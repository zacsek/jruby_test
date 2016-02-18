package de.changepoint.jruby.runner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.jruby.embed.ScriptingContainer;
import org.jruby.util.Dir;

import static com.google.common.base.Throwables.propagate;
import static java.lang.System.getenv;
import static org.jruby.CompatVersion.RUBY1_9;
import static org.jruby.embed.AttributeName.BASE_DIR;
import static de.changepoint.jruby.runner.JRubyRunnerOptions.buildOptions;

public class JRubyRunner {
  private final ScriptingContainer ruby;

  public JRubyRunner(JRubyRunnerOptions options) {
    ruby = new ScriptingContainer();
    ruby.setAttribute(BASE_DIR, options.getWorkingDirectory());
    ruby.setCompatVersion(RUBY1_9);
    ruby.setCurrentDirectory(options.getWorkingDirectory());
    ruby.setEnvironment(options.getEnvironment());
    ruby.setLoadPaths(options.getLoadPaths());
  }

  public void run() {
    //ruby.runScriptlet("begin require 'helloworld'; HelloWorld.run; rescue Exception => e; puts e; raise; end;");
	ruby.runScriptlet("r = require 'helloworld'; puts \"Loaded: #{r.to_s}\"");
  }

  public static void main(String... args) throws Exception {
    new JRubyRunner(buildOptions(args)).run();
  }
}
