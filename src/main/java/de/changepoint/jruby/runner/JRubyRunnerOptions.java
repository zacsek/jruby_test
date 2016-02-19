package de.changepoint.jruby.runner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static java.lang.System.getenv;

public class JRubyRunnerOptions {
	@Parameter(names = {"--with-system-gems", "-w"}, description = "Include System Gems.")
	private Boolean withSystemGems = false;

	private Settings settings;

	private JRubyRunnerOptions() {}

	public static JRubyRunnerOptions buildOptions(String[] args) throws Exception {
		JRubyRunnerOptions options = new JRubyRunnerOptions();

		parseOptions(args, options);

		if (options.withSystemGems) {
			options.settings = new SystemGemSettings();
		} else {
			String home_path = new JarExtractor().extractFromJar("home");
			options.settings = new PackagedGemSettings(home_path);
		}

		return options;
	}

	private static void parseOptions(String[] args, JRubyRunnerOptions options) {
		JCommander parser = new JCommander(options);
		parser.setProgramName(JRubyRunner.class.getSimpleName());

		try {
			parser.parse(args);
		} catch (ParameterException e) {
			StringBuilder buffer = new StringBuilder();
			parser.usage(buffer);
			System.err.println(buffer);
			System.exit(1);
		}
	}

	Map<String, String> getEnvironment() {
		return ImmutableMap.<String, String>builder()
			.put("GEM_HOME", settings.getGemHome())
			.put("GEM_PATH", settings.getGemPath())
			.put("HOME", settings.getHome())
			.build();
	}

	List<String> getLoadPaths() {
		return settings.getLoadPaths();
	}

	String getWorkingDirectory() {
		String path = jarPath();
		return path.substring(0, path.lastIndexOf("/"));
	}

	public static abstract class Settings {
		abstract String getGemHome();
		abstract String getGemPath();
		abstract String getHome();
		abstract List<String> getLoadPaths();
	}

	static class SystemGemSettings extends Settings {
		@Override String getGemHome() {
			return getenv("GEM_HOME");
		}

		@Override String getGemPath() {
			return getenv("GEM_PATH");
		}

		@Override String getHome() {
			return getenv("HOME");
		}

		@Override List<String> getLoadPaths() {
			return ImmutableList.of();
		}
	}

	static class PackagedGemSettings extends Settings {
		private final String homePath;
		private final String jar;

		public PackagedGemSettings(String home_path) {
			this.homePath = home_path;
			try {
				String self = "/" + PackagedGemSettings.class.getName().replace('.','/') + ".class";
				String path = getClass().getResource(self).toURI().getSchemeSpecificPart();
				jar = path.replace("!" + self, "").replace("file:","");
			}
			catch(URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		@Override String getGemHome() {
			return jar + "!/rubygems";
		}

		@Override String getGemPath() {
			return jar + "!/rubygems";
		}

		@Override String getHome() {
			return homePath;
		}

		@Override List<String> getLoadPaths() {
			return ImmutableList.of();
		}
	}

	// http://stackoverflow.com/questions/2837263/how-do-i-get-the-directory-that-the-currently-executing-jar-file-is-in
	private static String jarPath() {
		URL url = JRubyRunner.class.getProtectionDomain().getCodeSource().getLocation();
		return url.getPath();
	}
}
