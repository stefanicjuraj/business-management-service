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

}
