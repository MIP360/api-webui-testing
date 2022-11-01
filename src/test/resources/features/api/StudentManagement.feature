Feature: Student Management

  Background: Initialize test data
    Given Store a value:"int~15" in Variable:"[StudentId]"
    Given Store a value:"int~1511" in Variable:"[InvalidStudentId]"


  @api-testing @create-new-student @positive-scenario
  Scenario: Create New Student
    Given Prepare Json Object payload
      |firstName   |john        |
      |id          |[StudentId] |
      |lastName    |karmer      |
      |nationality |american    |
      |studentClass|mathmatics  |
    Then Send POST Request to endpoint:"/studentmgmt/addStudent"
    And The API should return status 200
    And Validate the String Response
      |New student enrolled with student id : [StudentId]|

  @api-testing @update-student @positive-scenario
  Scenario: Update a student
    Given Prepare Json Object payload
      |firstName   |Ray         |
      |id          |[StudentId] |
      |lastName    |Jose        |
      |nationality |american    |
      |studentClass|compture    |
    Then Send PUT Request to endpoint:"/studentmgmt/updateStudent"
    And The API should return status 200
    And Validate JSON Object inline records in api response
      |firstName |id          |lastName   |nationality|studentClass|
      |Ray       |[StudentId] |Jose       |american   |compture    |


  @api-testing @get-students @positive-scenario
  Scenario: Get all students
    Given Send GET Request to endpoint:"/studentmgmt/fetchStudents"
    And The API should return status 200
    And Validate JSON Array inline records in api response
      |firstName |id          |lastName   |nationality|studentClass|
      |Ray       |[StudentId] |Jose       |american   |compture    |

  @api-testing @get-students @positive-scenario
  Scenario: Get student by id
    Given Prepare Query Parameters
      |id  |[StudentId] |
    Then Send GET Request to endpoint:"/studentmgmt/fetchStudents"
    And The API should return status 200
    And Validate JSON Array inline records in api response
      |firstName |id          |lastName   |nationality|studentClass|
      |Ray       |[StudentId] |Jose       |american   |compture    |

  @api-testing @get-students @positive-scenario
  Scenario: Get student by studentClass
    Given Prepare Query Parameters
      |studentClass  |compture |
    Then Send GET Request to endpoint:"/studentmgmt/fetchStudents"
    And The API should return status 200
    And Validate JSON Array inline records in api response
      |firstName |id          |lastName   |nationality|studentClass|
      |Ray       |[StudentId] |Jose       |american   |compture    |

  @api-testing @get-students @positive-scenario
  Scenario: Get student by id,studentClass
    Given Prepare Query Parameters
      |id            |[StudentId] |
      |studentClass  |compture    |
    Then Send GET Request to endpoint:"/studentmgmt/fetchStudents"
    And The API should return status 200
    And Validate JSON Array inline records in api response
      |firstName |id          |lastName   |nationality|studentClass|
      |Ray       |[StudentId] |Jose       |american   |compture    |

  @api-testing @delete-student @positive-scenario
  Scenario: Delete student
    Given Prepare Json Object payload
      |id          |[StudentId] |
    Then Send DELETE Request with payload to endpoint:"/studentmgmt/deleteStudent"
    And The API should return status 200
    Given Prepare Query Parameters
      |id            |[StudentId] |
    Then Send GET Request to endpoint:"/studentmgmt/fetchStudents"
    And The API should return status 200
    And Validate JSON Array Response count:0

  @api-testing @create-new-student @negative-scenario
  Scenario Outline: Create New Student without mandatory field "<scenario>"
    Given Set Json Object String as payload:'<input>'
    Then Send POST Request to endpoint:"/studentmgmt/addStudent"
    And The API should return status <httpStatusCode>
    Examples:
      |scenario         | input                                                                                                  | httpStatusCode |
      | no id           |""" {"firstName":"king2","lastName":"peterson","studentClass":"computer","nationality":"american"} """  | 400            |
      | no firstName    |""" {"id":0,"lastName":"peterson","studentClass":"computer","nationality":"american"} """               | 400            |
      | no studentClass |""" {"id":0,"firstName":"king2","lastName":"peterson","nationality":"american"} """                     | 400            |

  @api-testing @update-student @negative-scenario
  Scenario Outline: Update Student without mandatory field "<scenario>"
    Given Set Json Object String as payload:'<input>'
    Then Send PUT Request to endpoint:"/studentmgmt/updateStudent"
    And The API should return status <httpStatusCode>
    Examples:
      |scenario         | input                                                                                                  | httpStatusCode |
      | no id           |""" {"firstName":"king2","lastName":"peterson","studentClass":"computer","nationality":"american"} """  | 400            |
      | no firstName    |""" {"id":0,"lastName":"peterson","studentClass":"computer","nationality":"american"} """               | 400            |
      | no studentClass |""" {"id":0,"firstName":"king2","lastName":"peterson","nationality":"american"} """                     | 400            |

  @api-testing @delete-student @negative-scenario
  Scenario: Delete student with student id not present
    Given Prepare Json Object payload
      |id          |[InvalidStudentId] |
    Then Send DELETE Request with payload to endpoint:"/studentmgmt/deleteStudent"
    And The API should return status 500





