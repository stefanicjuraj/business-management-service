package businesslayer;

import java.util.List;

public class EmployeeBusiness extends Business {

    /**
     * Creates a new instance of the 'EmployeeBusiness' class. It inherits the
     * initialization of data access and Gson objects from the parent 'Business'
     * class to maintain consistency across the application's business logic.
     */
    public EmployeeBusiness() {
        super(); // Calls the constructor of the parent class.
    }

    /**
     * Retrieves a list of employees for a given company.
     *
     * @param companyName the name of the company of which employees are to be
     *                    retrieved.
     * @return a JSON response containing a list of employees or an error
     *         message if no employees are found.
     */
    public Response getAll(String companyName) {
        try {
            // Retrieve all employees for the given company name
            List<Employee> employees = this.dl.getAllEmployee(companyName);

            // Check if the employees list is empty or if the company name does not match
            // the expected value
            if (employees.isEmpty() || !companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return an OK response with a message indicating no employees were found for
                // the company
                return Response.status(Response.Status.OK)
                        .entity("{\"error\": \"No employees found for the company name provided: " + companyName
                                + ".\"}")
                        .build();
            }

            // Return an OK response with the list of employees
            return Response.status(Response.Status.OK).entity(employees).build();

        } catch (Exception e) {
            // Return an Internal Server Error response with a message indicating a database
            // connection failure
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to connect to the database: " + e.getMessage() + ".\"}").build();
        }
    }

    /**
     * Retrieves information about a specific employee.
     *
     * @param empId The unique identifier of the employee.
     * @return A JSON response containing the employee's information or an error
     *         message if the employee is not found.
     */
    public Response get(int empId) {
        try {
            // Attempt to retrieve the employee by their ID
            Employee employee = this.dl.getEmployee(empId);
            if (employee == null) {
                // Respond with OK status but indicate that no employee was found with the
                // provided ID
                return Response.status(Response.Status.OK)
                        .entity("{\"error\":\"No employee found with ID: " + empId + ".\"}").build();
            }
            // Successfully found the employee, respond with OK status and the employee data
            return Response.status(Response.Status.OK).entity(employee).build();
        } catch (Exception e) {
            // Respond with an Internal Server Error status and the error message
            // encountered
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to retrieve the employee data. Reason: " + e.getMessage() + "\"}")
                    .build();
        }
    }

}
