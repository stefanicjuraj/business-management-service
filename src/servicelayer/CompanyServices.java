package servicelayer;

@Path("CompanyServices")
public class CompanyServices {

    private DepartmentBusiness departmentBusiness = null;
    private EmployeeBusiness employeeBusiness = null;

    /**
     * Creates a new instance of CompanyServices.
     *
     * It initializes the departmentBusiness and employeeBusiness objects, which
     * are used to interact with the business logic related to departments and
     * employees.
     */
    public CompanyServices() {
        this.departmentBusiness = new DepartmentBusiness();
        this.employeeBusiness = new EmployeeBusiness();
    }

    /**
     * Maps to HTTP GET requests with the path "/departments" relative to the
     * base path defined for the class (e.g., "/CompanyServices").
     *
     * It expects a query parameter "company" and produces a response in JSON
     * format.
     *
     * URI:
     * http://localhost:8080/MarasovicKP2/webresources/CompanyServices/departments?company=kxmzgr
     *
     * @param companyName the name of the company (your RIT username).
     * @return the list of all departments for the given company as a JSON
     *         response.
     */
    @GET
    @Path("departments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments(@QueryParam("company") String companyName) {
        return departmentBusiness.getAll(companyName);
    }

    /**
     * Maps to HTTP GET requests with the path "/department".
     *
     * It expects query parameters "company" and "dept_id" and produces a
     * response in JSON format. It calls the departmentBusiness.get(companyName,
     * deptId) method to retrieve information about a specific department based
     * on the company and department ID provided in the query parameters.
     *
     * @param companyName the name of the company (your RIT username).
     * @param deptId      the department ID.
     * @return the department as a JSON response.
     */
    @GET
    @Path("department")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(
            @QueryParam("company") String companyName,
            @QueryParam("dept_id") int deptId) {
        return departmentBusiness.get(companyName, deptId);
    }

    /**
     * Maps to HTTP POST requests with the path "/department".
     *
     * It consumes data in the form of application/x-www-form-urlencoded and
     * expects form parameters such as "company," "dept_name," "dept_no," and
     * "location".
     *
     * The departmentBusiness.insert(companyName, deptName, deptNo, location)
     * method is called to add a new department to the DB which then returns the
     * result as a JSON response.
     *
     * @param companyName the name of the company.
     * @param deptName    the name of the department.
     * @param deptNo      the department number.
     * @param location    the location of the department.
     * @return the newly created department as a JSON response.
     */
    @POST
    @Path("department")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDepartment(
            @FormParam("company") String companyName,
            @FormParam("dept_name") String deptName,
            @FormParam("dept_no") String deptNo,
            @FormParam("location") String location) {
        return departmentBusiness.insert(companyName, deptName, deptNo, location);
    }

}
