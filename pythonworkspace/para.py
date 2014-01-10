#!/usr/bin/python2.7
def printpara1(name,greeting):
    print '%s,%s' % (greeting,name)
def printpara2(name,greeting='world'):
    print '%s,%s' % (greeting,name)
def printpara3(title,*name):
    print title
    print name
printpara1('world','hello')
printpara2('hello')
printpara3('hello','mick','tracy','kate')
