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

### Building (maven/java)

To generate the api using the yaml-specifications and build the server use

    mvn clean install
    
### Running (java)

To start the server, launch the main class `de.fschili.dlx.DlxDemoServer` from your IDE

    de.fschili.dlx.DlxDemoServer
    
respectively lauch the generated jar from commandline using

    java -jar dicom-link-exchange.jar

The demo server will run at 

    http://localhost:3000/
    
and will respond to the defined requests.

### Documentation (Swagger)

The documentation can be browsed at 

    http://localhost:3000/swagger-ui/index.html
