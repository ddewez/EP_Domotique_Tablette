QT += core
QT += widgets
QT += serialport
QT -= gui

CONFIG += c++11

TARGET = Bluetooth_com
CONFIG += console
CONFIG -= app_bundle

TEMPLATE = app

SOURCES += main.cpp
