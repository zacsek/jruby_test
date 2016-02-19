puts "Home: #{ENV['HOME']}"
puts "GemHome: #{ENV['GEM_HOME']}"
puts "GemPath: #{ENV['GEM_PATH']}"

loaded = require 'awesome_print'
ap ENV

class HelloWorld
	def self.run
		hw = HelloWorld.new
	end

	attr_accessor :hash

	def initialize
		begin
			puts 'HelloWorld::initialize'
			@hash = { :hello => 'World', :nice => 2, :see => 'you' }
			puts @hash
			ap @hash
		rescue Exception => e
			puts e
			raise
		end
	end
end

puts "Loaded AP: #{loaded.to_s}"
HelloWorld.run
