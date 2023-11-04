# Business Management Service

<div align="justify">

Business Management Service is a RESTful web service written in Java, designed to provide a system for managing and tracking department and employee records for a company, following the 3-layered architecture - service layer, business layer, and data layer.

## Service Layer

The service layer acts as the entry point for the API requests. It is designed to be lightweight and to delegate business logic responsibilities to the business layer. This ensures that the service layer remains agnostic to the complexities of the application, facilitating scalability and security.

- **Path:** `/company`

  - **Verb:** DELETE
  - **Produces:** application/json
  - **Description:** Deletes all Department and Employee records in the database for the given company.

- **Path:** `/department`

  - **Verb:** GET
  - **Produces:** application/json
  - **Description:** Returns the requested Department as a JSON String.

- **Path:** `/department`

  - **Verb:** GET
  - **Produces:** application/json
  - **Description:** Returns the requested Department as a JSON String.

- **Path:** `/departments`

  - **Verb:** GET
  - **Produces:** application/json
  - **Description:** Returns the requested list of Departments.

- **Path:** `/department`

  - **Verb:** PUT
  - **Produces:** application/json
  - **Description:** Returns the updated Department as a JSON String.

- **Path:** `/department`

  - **Verb:** POST
  - **Produces:** application/json
  - **Description:** Returns the new Department as a JSON String.

- **Path:** `/department`

  - **Verb:** DELETE
  - **Produces:** application/json
  - **Description:** Returns the number of rows deleted.

- **Path:** `/employee`

  - **Verb:** GET
  - **Produces:** application/json
  - **Description:** Returns the requested Employee as a JSON String.

- **Path:** `/employees`

  - **Verb:** GET
  - **Produces:** application/json
  - **Description:** Returns the requested list of Employees.

- **Path:** `/employee`

  - **Verb:** POST
  - **Produces:** application/json
  - **Description:** Returns the new Employee as a JSON String.

- **Path:** `/employee`

  - **Verb:** PUT
  - **Produces:** application/json
  - **Description:** Returns the updated Employee as a JSON String.

- **Path:** `/employee`

  - **Verb:** DELETE
  - **Produces:** application/json
  - **Description:** Returns the that the employee deleted.

## Business Layer

The business layer contains the core logic for managing employee data. It performs necessary validations, processes business rules, and prepares data structures for communication with the data layer. This separation ensures that the business logic is centralized and can evolve independently of other layers.

## Data Layer

This layer interacts with the company database hosted on the server, performing CRUD (Create, Read, Update, Delete) operations as requested by the business layer. The data layer abstracts the underlying database interactions, providing a clean interface for data retrieval and manipulation.

</div>
