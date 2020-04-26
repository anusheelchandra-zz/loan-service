# Loan Service 

Loan Service is a microservice for generating loan plans. It has a RestFul API to integrate with.

### Topics
  * [What does it do ?](#technical-details)
  * [How to use it ?](#guide)
  * [Tech Stack](#tech-stack)
  


###  [What does it do ?](#technical-details)
This api generates loan plan for a given loan request and returns a plan for repayment of loan over the loan duration

###  [How to use it ?](#guide)
One can use this api very easily by hiting the RestFul endpoint which when provided with appropriate loan request will return a loan plan.

To connect with the endpoint (http://localhost:8080/generate-plan) you can refer to below Swagger Ui.
* [Swagger UI](http://localhost:8080/swagger-ui.html#/)

One can simply send a [loan request](#request) like one shown below to the above post endpoint and in return will receive a 
loan plan which is a list of loan repayments and would look similar to [loan response](#response)
.
###  [Sample Request](#request) :

`{
 	"durationInMonth": "1",
 	"nominalRate": "5",
 	"loanAmount": "5000",
 	"startDate": "2020-05-01T00:00:01Z"
 }`
 
###  [Sample Response](#response) :  

`[
     {
         "borrowerPaymentAmount": 5020.83,
         "date": "2020-05-01T00:00:01",
         "interest": 20.83,
         "principal": 5000,
         "initialOutstandingPrincipal": 5000,
         "remainingOutstandingPrincipal": 0
     }
 ]` 

###  [Tech Stack](#tech-stack)
We have used Java 8, Spring Boot an maven to build this microservice. 
It uses lombok, jUnit 5, mockito and assertJ library for code generation(getter/setter/builders etc) and testing purposes. 


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

