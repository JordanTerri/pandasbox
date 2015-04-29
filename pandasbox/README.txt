The application can establish a simple SIP-PBX and recorder features with possibilities of supervising few stuffs in a web-app.

- sippbx : sip implementation content
- recorder : recording features
- webapp : web-app allowing to supervize
- common-api : Dtos and others stuffs


recorder depends on common-api
sippbx depends on common-api and recorder
web-app depends on common api
