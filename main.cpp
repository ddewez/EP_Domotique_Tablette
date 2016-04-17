#include <QCoreApplication>
#include <QtWidgets/QApplication>
#include <QtSerialPort/qserialport.h>
#include <QtSerialPort/qserialportinfo.h>
#include <QtSerialPort/QSerialPort>
#include <QtSerialPort/QSerialPortInfo>
#include <QDebug>
#include <iostream>

using namespace std;


int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);

    // qDebug : output stream for debugging information
    // For each port, print information
    QSerialPort *port = new QSerialPort("COM9");
    if(port->open(QIODevice::ReadWrite)){
        qDebug() << "Name :" << port->portName();
        if(port->isOpen() && port->isWritable()){
            const char output[10] = "AT+WGVP\r\n";
            port->write(output);
            port->flush();
            QByteArray input = port->readAll();
            while (port->waitForReadyRead(2000)) {
               input.append(port->readAll());
            }
            qDebug() << input;
        }
        port->close();
    } else {
        cout << "Erreur" << endl;
    }
    /*foreach (const QSerialPortInfo &serialPortInfo, QSerialPortInfo::availablePorts()) {

        QSerialPort *port = new QSerialPort(serialPortInfo);
        if (port->open(QIODevice::ReadWrite) && port->portName()=="COM9") {

            qDebug() << "Name :" << port->portName();
            qDebug() << "Baud rate:" << port->baudRate();
            qDebug() << "Data bits:" << port->dataBits();
            qDebug() << "Stop bits:" << port->stopBits();
            qDebug() << "Parity:" << port->parity();
            qDebug() << "Flow control:" << port->flowControl();
            qDebug() << "Read buffer size:" << port->readBufferSize();
            if(port->isOpen() && port->isWritable()){
                const char output[10] = "AT+WGVP\r\n";
                port->write(output);
                port->flush();
                QByteArray input = port->readAll();
                while (port->waitForReadyRead(2000)) {
                   input.append(port->readAll());
                }
                qDebug() << input;
            }
            port->close();

        }
        else {
            // If problem
            qDebug() << "Unable to open port, error code" << port->error();
        }


        delete port;
    }
*/

    return 0;
}
