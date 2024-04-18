DICOM Link Exchange (DLX)
=========================

A collaborative project to work on the specifications for the DIN workgroup "QR-Codes/Online-Bereitstellung von Bilddaten" resp. DIN-Normenausschuss Radiologie (NAR) NA 080-00-04-10 AK "Online Bereitstellung von Bilddaten".

The main goal is to provide an [OpenAPI](https://www.openapis.org/) specification to support the specified workflow.

## OpenApi

This project uses [OpenAPI](https://www.openapis.org/) (v3) for all specifications. 

To edit and preview the specification, the `dicomLinkExchange.yaml` can be opened with e.g. the [Swagger Editor](https://editor.swagger.io/). 

## Demo Server

This project includes a minimal demo server for prototyping of the api and specifications.

**THE IMPLEMENTATION OF THE DEMO SERVER IN NOT COMPLETE AND ONLY FOR DEMO PURPOSES!**

### Available token

Following demo token are available (hard coded).

###### Birthday-Token: ABC-S1Z-98A   
    Question: "Wann haben Sie Geburtstag?"
    Answer: "19700102"

###### Custom-Token: HDF-34F-HK6
    Question: "Wann war ihre Untersuchung?"
    Answer: "20240203"
 
    Question:  "Wie heißt Ihr behandelnder Arzt?"
    Answer: "Dr. Mayer"

##### Building (maven/java)

To generate the api using the yaml-specifications and build the server use

    mvn clean compile package
    
### Running (java)

To start the server, launch the main class `de.fschili.dlx.DlxDemoServer` from your IDE

    de.fschili.dlx.DlxDemoServer
    
or via spring-boot with

    mvn spring-boot:run  
    
respectively lauch the generated jar from commandline using

    java -jar dicom-link-exchange.jar

The demo server will run at 

    http://localhost:3000/
    
and will respond to the defined requests.

Konfiguration can be made at

    resources/application.properties

### Documentation (Swagger)

The documentation can be browsed at 

    http://localhost:3000/swagger-ui/index.html
