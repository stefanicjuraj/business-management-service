package businesslayer;

public class DepartmentBusiness extends Business {

    /**
     * Creates a new instance of the 'DepartmentBusiness' class. It inherits the
     * initialization of data access and Gson objects from the parent 'Business'
     * class to maintain consistency across the application's business logic.
     */
    public DepartmentBusiness() {
        super(); // Calls the constructor of the parent class.
    }

    /**
     * Retrieves a list of departments for a given company.
     *
     * @param companyName the name of the company for which departments are to
     *                    be retrieved.
     * @return a JSON response containing a list of departments or an error
     *         message if no departments are found.
     */
    public Response getAll(String companyName) {

        try {
            // Attempt to retrieve all departments for the given company name.
            List<Department> departments = this.dl.getAllDepartment(companyName);

            // Check if no departments were found or if the given company name does not
            // match the expected one.
            if (departments.isEmpty() || !companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return a response indicating that no departments were found for the specified
                // company.
                return Response.status(Response.Status.OK)
                        .entity("{\"error\": \"No departments found for the specified company: " + companyName
                                + ".\"}")
                        .build();
            }

            // If departments were found, return them in the response.
            return Response.status(Response.Status.OK).entity(departments).build();
        } catch (Exception e) {
            // In case of a database connection failure, return an error message.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Unable to connect to the database. Detailed message: " + e.getMessage()
                            + "\"}")
                    .build();
        }
    }

    /**
     * Retrieves information about a specific department.
     *
     * @param companyName The name of the company.
     * @param deptId      The unique identifier of the department.
     * @return A JSON response containing the department's information or an
     *         error message if the department is not found.
     */
    public Response get(String companyName, int deptId) {
        try {
            // Validate the company name against the business configuration.
            if (!companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Return a standardized error message when the company name is invalid.
                return Response.status(Response.Status.BAD_REQUEST) // Using BAD_REQUEST for invalid inputs.
                        .entity("{\"error\":\"Invalid company name provided: " + companyName + ". Expected: "
                                + BusinessConfig.COMPANY_NAME + ".\"}")
                        .build();
            }

            // Retrieve the department by ID for the given company name.
            Department department = this.dl.getDepartment(companyName, deptId);
            if (department == null) {
                // Return a standardized error message when no department is found for the given
                // ID.
                return Response.status(Response.Status.NOT_FOUND) // Using NOT_FOUND for nonexistent resources.
                        .entity("{\"error\":\"Department ID " + deptId + " does not exist for company " + companyName
                                + ".\"}")
                        .build();
            }

            // Successfully return the department entity if found.
            return Response.status(Response.Status.OK).entity(department).build();
        } catch (Exception e) {
            // Return a standardized error message in case of a database connection failure.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Database connection failed. Error details: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Inserts a new department into the system.
     *
     * @param companyName The name of the company.
     * @param deptName    The name of the new department.
     * @param deptNo      The department number.
     * @param location    The location of the new department.
     * @return A JSON response containing the new department's information or an
     *         error message.
     */
    public Response insert(String companyName, String deptName, String deptNo, String location) {
        try {
            // Validate the company name to match business configuration.
            if (!companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Respond with an error if the company name does not match.
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid company name provided. Expected: '" + BusinessConfig.COMPANY_NAME
                                + "' but received: '" + companyName + "'.\"}")
                        .build();
            }

            // Check for the uniqueness of the department number.
            Department existingDept = this.dl.getDepartmentNo(companyName, deptNo);
            if (existingDept != null) {
                // Respond with an error if the department number is already in use.
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Department number '" + deptNo
                                + "' is already in use. Please provide a unique department number.\"}")
                        .build();
            }

            // Instantiate and set properties for the new department.
            Department newDept = new Department();
            newDept.setCompany(companyName);
            newDept.setDeptName(deptName);
            newDept.setDeptNo(deptNo);
            newDept.setLocation(location);

            // Attempt to insert the new department.
            Department insertedDept = this.dl.insertDepartment(newDept);
            if (insertedDept == null) {
                // Respond with an error if the department could not be inserted.
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Insertion of the new department failed. Please try again later or contact support.\"}")
                        .build();
            }

            // Return the successfully inserted department details.
            return Response.status(Response.Status.CREATED).entity(gson.toJson(
                    Collections.singletonMap("success", insertedDept))).build();
        } catch (Exception e) {
            // Respond with an error in case of an unexpected exception.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage()
                            + ". Please contact support.\"}")
                    .build();
        }
    }

    /**
     * Updates department information based on a JSON representation.
     *
     * @param departmentJson A JSON representation of the department.
     * @return A JSON response containing the updated department information or
     *         an error message.
     */
    public Response update(String departmentJson) {

        try {

            // Convert the JSON input into a Department object.
            Department updatedDepartment = gson.fromJson(departmentJson, Department.class);

            // Check if the department number already exists to avoid duplicates.
            Department existingDeptWithSameNo = this.dl.getDepartmentNo(updatedDepartment.getCompany(),
                    updatedDepartment.getDeptNo());
            if (existingDeptWithSameNo != null
                    && (existingDeptWithSameNo.getDeptNo() == null ? updatedDepartment.getDeptNo() != null
                            : !existingDeptWithSameNo.getDeptNo().equals(updatedDepartment.getDeptNo()))) {
                // Respond with a conflict error if the department number is not unique.
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Department number '" + updatedDepartment.getDeptNo()
                                + "' is already assigned to a different department.\"}")
                        .build();
            }

            // Attempt to update the department in the database.
            Department resultDepartment = this.dl.updateDepartment(updatedDepartment);

            // Check if the department update was unsuccessful.
            if (resultDepartment == null) {
                // Respond with a not found error if the department could not be updated.
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"The specified department does not exist or could not be updated.\"}")
                        .build();
            } else {
                // Return the updated department data.
                return Response.status(Response.Status.OK).entity(resultDepartment).build();
            }
        } catch (JsonSyntaxException e) {
            // Handle JSON parsing errors and respond with a server error message.
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Department update failed due to invalid JSON format: " + e.getMessage()
                            + "\"}")
                    .build();
        }
    }

    /**
     * Deletes a department with the given ID.
     *
     * @param companyName The name of the company.
     * @param deptId      The unique identifier of the department to be deleted.
     * @return A JSON response indicating success or an error message.
     */
    public Response delete(String companyName, int deptId) {
        try {
            // Validate the company name against the business configuration.
            if (!companyName.equals(BusinessConfig.COMPANY_NAME)) {
                // Respond with an error if the company name does not match the expected value.
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid company name provided. Expected: '" + BusinessConfig.COMPANY_NAME
                                + "', but got: '" + companyName + "'.\"}")
                        .build();
            }

            // Check if the department exists within the specified company.
            Department existingDept = this.dl.getDepartment(companyName, deptId);
            if (existingDept == null) {
                // Respond with an error if the department does not exist.
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Department with ID '" + deptId + "' not found in company '" + companyName
                                + "'.\"}")
                        .build();
            }

            // Perform the deletion of the department.
            int deletedRows = this.dl.deleteDepartment(companyName, deptId);
            if (deletedRows == 0) {
                // Respond with an error if no rows were affected in the deletion process.
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"No changes made. Department with ID '" + deptId
                                + "' could not be deleted or may already have been removed.\"}")
                        .build();
            }

            // Confirm the successful deletion of the department.
            return Response.status(Response.Status.OK)
                    .entity("{\"success\":\"Department with ID '" + deptId + "' successfully deleted from company '"
                            + companyName + "'.\"}")
                    .build();
        } catch (Exception e) {
            // Respond with a general error message for any other exceptions.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred while attempting to delete the department: "
                            + e.getMessage() + ".\"}")
                    .build();
        }
    }

}
