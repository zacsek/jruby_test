#!/usr/bin/env bash
LOCAL_GEM_REPO=`pwd`/../rubygems
export GEM_PATH=$LOCAL_GEM_REPO
export GEM_HOME=$LOCAL_GEM_REPO
gem install -g Gemfile
