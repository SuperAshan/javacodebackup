#!/usr/bin/python2.7
def with_stars(**kwds):
    print kwds['name'],'is',kwds['age'],'year old'
def without_stars(kwds):
    print kwds['name'],'is',kwds['age'],'year old'
args={'name':'Mr. Gumby','age':42}
with_stars(**args)
without_stars(args)

   
