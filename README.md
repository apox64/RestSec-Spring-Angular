# RestSec - Vulnerability Scanner for Developers

## User Manual
- [Installation](#Installation)
- [Usage](#Usage)
- [Technology Stack (for developers)](#Technology Stack)
- [Architecture (for developers)](#Architecture)

## Installation <a name="Installation"></a>
1. install and start an instance of [OWASP ZAP](https://github.com/zaproxy/zaproxy)
2. clone this git repository
3. build and run the ``backend`` folder with Java 8 (or higher)
4. build and run the ``frontend`` folder with Node.js (``npm start``)

``DISCLAIMER: A local proxy was used during development. IP addresses might not be correct for your situation. Please change them in the code, so the frontend can talk to the backend.``

## Usage <a name="Usage"></a>
1. Add some endpoints you want to scan on the "Endpoints" tab
2. You can let __RestSec__ detect endpoints automatically under the "Crawler" tab (upload a _Swagger_ API documentation file (\*.yaml, \*.json) or let a crawler recursively browse through a _HATEOAS_ Link)
3. Under the "Scanner" tab you can select the types of scanners you want to run against the endpoints.
4. Hit ``FIRE`` to start the scanners against the endpoints. Lean back and wait.
![3_scanner_screenshot.png](https://raw.githubusercontent.com/apox64/RestSec-Spring-Angular/master/doc/screenshots/3_scanner_screenshot.png)
5. Once the scanners have finished, go to the "Results" tab to see the results.

``DISCLAIMER: Currently only the "OWASP ZAP Proxy" is implemented. More scanners are planned.``

## Technology Stack <a name="Technology Stack"></a>
Backend:
- Java 8
- Spring Boot

Frontend:
- Node.js
- Angular 4
- Material Design

## Architecture <a name="Architecture"></a>
No content yet.
