/*
 * Created by Alexey Yarkov on 25.10.17
 * Copyright © 2017 Alexey Yarkov. All rights reserved.
 */

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.xml.sax.*;

public class LR5 {
    public static void main(String[] args){
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        CarShop A = new CarShop();
        try
        {
            dbf = DocumentBuilderFactory.newInstance();//создаём специальную фабрику паресеров
            dbf.setValidating(true); //включаем проверку структуры документа
            db = dbf.newDocumentBuilder();//создаём парсер для документа
            db.setErrorHandler(new SimpleErrorHandler()); //создаём экземпляр обработчика
            Document document = db.parse(new File("LR5_dtd.xml")); //создавём документ на основе файла xml

            NodeList carShop = document.getElementsByTagName("Car"); // Получаем корневой элемент
            for (int i=0; i< carShop.getLength(); i++){
                if (carShop.item(i).getNodeType() == Node.ELEMENT_NODE)
                {
                    Car T = new Car();
                    Element carShopElement = (Element) carShop.item(i);

                    NodeList car = carShopElement.getChildNodes();
                    for (int j=0; j<car.getLength(); j++){
                        if (car.item(j).getNodeType() == Node.ELEMENT_NODE){
                            Element carElement = (Element) car.item(j);
                            switch (carElement.getNodeName().trim()){
                                case "ID":{
                                    T.setID(Integer.parseInt(carElement.getTextContent().trim()));
                                }break;
                                case "Model":{
                                    T.setModel(carElement.getTextContent().trim());
                                }break;
                                case "Country":{
                                    T.setCountry(carElement.getTextContent().trim());
                                }break;
                                case "Year":{
                                    T.setYear(Integer.parseInt(carElement.getTextContent().trim()));
                                }break;
                                case "V":{
                                    T.setV(Integer.parseInt(carElement.getTextContent().trim()));
                                }break;
                                case "Price":{
                                    T.setPrice(Integer.parseInt(carElement.getTextContent().trim()));
                                }break;
                            }
                        }
                    }A.addCar(T);
                }
            }A.bubbleSortCar();
        } catch(ParserConfigurationException e)
        {
            System.out.println("Ошибка:\n\tСтек:");
            e.printStackTrace();
            System.out.println("\t"+e.getMessage()+"\n");
        } catch (SAXException ex) {
            System.out.println("Ошибка:");
            System.out.println("\t"+ex.getMessage()+"\n");
        } catch (IOException ex) {
            System.out.println("Ошибка:");
            System.out.println("\t"+ex.getMessage()+"\n");
        }
        try
        {
            dbf = DocumentBuilderFactory.newInstance();//создаём специальную фабрику паресеров
            db = dbf.newDocumentBuilder();//создаём парсер для нового чистого документа
            Document doc = db.newDocument();
            Car T[] = A.getCarShop();

            Element carShop = doc.createElement("CarShop"); // Создаем корневой элемент CarShop
            doc.appendChild(carShop); // Присоединяем корневой элемент CarShop

            Element carProp[][] = new Element[A.getS()][6]; // Создаём массив автомобильный свойств
            for (int i=0; i<A.getS(); i++) {
                Element car;
                car = doc.createElement("Car"); // Создаем вложенный элемент car
                carShop.appendChild(car); // Присоединяем вложенный элемент car
                //Создаем и присоединяем вложенные элементы carProp
                carProp[i][0] = doc.createElement("ID");
                carProp[i][0].appendChild(doc.createTextNode(String.valueOf(T[i].getID())));
                car.appendChild(carProp[i][0]);
                carProp[i][1] = doc.createElement("Model");
                carProp[i][1].appendChild(doc.createTextNode(String.valueOf(T[i].getModel())));
                car.appendChild(carProp[i][1]);
                carProp[i][2] = doc.createElement("Country");
                carProp[i][2].appendChild(doc.createTextNode(String.valueOf(T[i].getCountry())));
                car.appendChild(carProp[i][2]);
                carProp[i][3] = doc.createElement("Year");
                carProp[i][3].appendChild(doc.createTextNode(String.valueOf(T[i].getYear())));
                car.appendChild(carProp[i][3]);
                carProp[i][4] = doc.createElement("V");
                carProp[i][4].appendChild(doc.createTextNode(String.valueOf(T[i].getV())));
                car.appendChild(carProp[i][4]);
                carProp[i][5] = doc.createElement("Price");
                carProp[i][5].appendChild(doc.createTextNode(String.valueOf(T[i].getPrice())));
                car.appendChild(carProp[i][5]);
            }
            Source domSource = new DOMSource(doc);
            Result fileResult = new StreamResult(new File("LR5_out.xml"));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "CarShop.dtd");
            transformer.transform(domSource, fileResult);
        } catch(ParserConfigurationException e)
        {
            System.out.println("Ошибка:\n\tСтек:");
            e.printStackTrace();
            System.out.println("\t"+e.getMessage()+"\n");
        } catch (TransformerConfigurationException e) {
            System.out.println("Ошибка:\n\tСтек:");
            e.printStackTrace();
            System.out.println("\t"+e.getMessage()+"\n");
        } catch (TransformerException e) {
            System.out.println("Ошибка:\n\tСтек:");
            e.printStackTrace();
            System.out.println("\t"+e.getMessage()+"\n");
        }
    }
    static class SimpleErrorHandler implements ErrorHandler {
        // метод для обработки предупреждений
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Предупреждение:\n\tСтрока " +e.getLineNumber() + ":");
            System.out.println("\t"+e.getMessage()+"\n");
        }
        // метод для обработки ошибок
        public void error(SAXParseException e) throws SAXException {
            System.out.println("Ошибка:\n\tСтрока " +e.getLineNumber() + ":");
            System.out.println("\t"+e.getMessage()+"\n");
        }
        // метод для обработки критических ошибок
        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Критическая ошибка:\n\tСтрока " +e.getLineNumber() + ":");
            System.out.println("\t"+e.getMessage()+"\n");
        }
    }
}
