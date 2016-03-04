#!/usr/bin/env ruby
require 'nokogiri'
require 'awesome_print'

def build_dependency_path(node)
	group    = node.xpath('groupId').children[0].to_s
	artifact = node.xpath('artifactId').children[0].to_s
	version  = node.xpath('version').children[0].to_s
	result   = "%s:%s:%s" % [group, artifact, version]

	if node.xpath('type')
		type = node.xpath('type').children[0].to_s
		result += ":#{type}"
	end
	result
end


def download_dependency(dep, adjust)
	repos = { mvn: 'http://repo1.maven.org/maven2/', gem: 'http://rubygems-proxy.torquebox.org/releases/' }
	if dep != /^rubygems/
		repo = repos[:mvn]
	else
		repo = repos[:gem]
	end

	print "Downloading #{dep}...".ljust(adjust-10)
	`mvn -Dmaven.repo.local=#{$local_repo} org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl='#{repo}' -Dartifact=#{dep}`
	puts ($? == 0 ? 'OK' : 'FALSE').rjust(10)
end

$local_repo = "#{`pwd`.strip}/repo" 
Dir.mkdir($local_repo) unless Dir.exists? $local_repo

xml = Nokogiri::XML(File.open("pom.xml")).remove_namespaces!

deps  = []
deps += xml.xpath("//plugin").map { |n| build_dependency_path(n) }
deps += xml.xpath("//dependency").map { |n| build_dependency_path(n) }

screen = 100
puts "Downloading dependencies to: #{$local_repo}"
puts '.'*screen
deps.each { |dep|
	download_dependency(dep, screen)
}
puts '.'*screen
