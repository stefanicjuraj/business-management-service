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

}
