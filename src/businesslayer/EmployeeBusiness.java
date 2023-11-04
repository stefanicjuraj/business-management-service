package businesslayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * Inserts a new employee into the system.
     *
     * @param empName        The employee information.
     * @param empNo
     * @param hireDateString
     * @param job
     * @param salary
     * @param deptId
     * @param mngId
     * @return A JSON response containing the new department's information or an
     *         error message.
     */
    public Response insert(String empName, String empNo, String hireDateString, String job, Double salary, int deptId,
            int mngId) {

        try {
            // Parse the hire date from the provided string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date hireDate = sdf.parse(hireDateString);

            // Validate that the hire date is not set in the future
            Date currentDate = new Date();
            if (hireDate.after(currentDate)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Hire date cannot be in the future.\"}").build();
            }

            // Validate the manager ID if provided
            if (mngId != 0) {
                Response mngResponse = this.get(mngId);
                if (mngResponse.getStatus() != Response.Status.OK.getStatusCode()) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"Invalid manager ID: No matching manager found.\"}").build();
                }
            }

            // Check that the hire date is not on a weekend
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hireDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Hire date cannot be on a Saturday or Sunday.\"}").build();
            }

            // Convert the util Date to sql Date
            java.sql.Date sqlHireDate = new java.sql.Date(hireDate.getTime());

            // Create a new employee object and attempt to insert it
            Employee newEmployee = new Employee(empName, empNo, sqlHireDate, job, salary, deptId, mngId);
            Employee employee = dl.insertEmployee(newEmployee);
            if (employee == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Employee could not be created.\"}").build();
            }

            // Convert the inserted employee object to JSON
            String jsonEmployee = gson.toJson(employee);
            return Response.status(Response.Status.CREATED).entity(jsonEmployee).build();

        } catch (ParseException e) {
            // Respond with a BAD_REQUEST status if the date format is invalid
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid date format for hire date. Expected format: 'yyyy-MM-dd'.\"}")
                    .build();
        } catch (Exception e) {
            // Respond with an INTERNAL_SERVER_ERROR status for any other exceptions
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Updates employee information based on a JSON representation.
     *
     * @param employeeJson A JSON representation of the employee.
     * @return A JSON response containing the updated employee information or an
     *         error message.
     */
    public Response update(String employeeJson) {
        // Check if the employee JSON is null or empty
        if (employeeJson == null || employeeJson.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"The employee JSON payload cannot be empty or null.\"}").build();
        }

        try {
            // Deserialize the JSON to an Employee object
            Employee updatedEmployee = gson.fromJson(employeeJson, Employee.class);

            // Validate the ID of the employee
            if (updatedEmployee.getId() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"The ID value provided is invalid.\"}").build();
            }

            // Validate the employee name
            String name = updatedEmployee.getEmpName();
            if (name == null || name.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"The employee name is required and cannot be blank.\"}").build();
            } else if (name.length() > 50) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"The employee name cannot exceed 50 characters.\"}").build();
            }

            // Attempt to update the employee in the database
            Employee resultEmployee = this.dl.updateEmployee(updatedEmployee);

            // Check if the update operation was successful
            if (resultEmployee == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Employee not found or could not be updated.\"}").build();
            } else {
                // Successfully updated the employee, return the updated object
                return Response.status(Response.Status.OK).entity(resultEmployee).build();
            }

        } catch (JsonSyntaxException e) {
            // Catch JSON parsing errors and return an appropriate error message
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"The JSON format is invalid: " + e.getMessage() + ".\"}").build();
        }
    }

}
