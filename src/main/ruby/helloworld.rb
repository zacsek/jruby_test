#require 'rubygems'
require 'pry'
binding.pry
loaded = require 'awesome_print'

class HelloWorld
	def self.run
		hw = HelloWorld.new
	end

	attr_accessor :hash

	def initialize
		puts 'HelloWorld::initialize'
		self.hash = { :hello => 'World', :nice => 2, :see => 'you' }
		ap self.hash
	rescue Exception => e
		ap e
		raise
	end
end

puts "Loaded AP: #{loaded.to_s}"
HelloWorld.run
