#include <QCoreApplication>
#include <QtWidgets/QApplication>
#include <QtSerialPort/qserialport.h>
#include <QtSerialPort/qserialportinfo.h>
#include <QtSerialPort/QSerialPort>
#include <QtSerialPort/QSerialPortInfo>
#include <QDebug>
#include <iostream>
#include <regex>
#include <boost/lexical_cast.hpp>

using namespace std;

int getJoystickPositionX(QSerialPort *port){
    int x = 15;
    port->open(QIODevice::ReadWrite);
        if(port->isOpen() && port->isWritable()){
            const char output[10] = "AT+WGVP\r\n";
            port->write(output);
            port->flush();
            QByteArray input = port->readAll();
            while (port->waitForReadyRead(2000)) {
               input.append(port->readAll());
            }
            qDebug() << input;
            std::regex pattern { "(\\d+),\\d+" };
            std::string target { input.toStdString() };
            std::smatch match;
            std::regex_search(target, match, pattern);
            x = boost::lexical_cast<int>(match[1]);;


    } else {
        cout << "Erreur" << endl;
    }
    port->close();
    return x;
}

int getJoystickPositionY(QSerialPort *port){
    int y = 15;
    if(port->open(QIODevice::ReadWrite)){
        if(port->isOpen() && port->isWritable()){
            const char output[10] = "AT+WGVP\r\n";
            port->write(output);
            port->flush();
            QByteArray input = port->readAll();
            while (port->waitForReadyRead(2000)) {
               input.append(port->readAll());
            }
            qDebug() << input;
            std::regex pattern { "\\d+,(\\d+)" };
            std::string target { input.toStdString() };
            std::smatch match;
            std::regex_search(target, match, pattern);
            y = boost::lexical_cast<int>(match[1]);;
        }

    } else {
        cout << "Erreur" << endl;
    }
    port->close();
    return y;
}

int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    QSerialPort *port = new QSerialPort("COM9");
    int x = getJoystickPositionX(port);
    int y = getJoystickPositionY(port);
    cout << "Position du joystick = (" << x << ",";
    cout << y << ")" << endl;
    delete port;

}
