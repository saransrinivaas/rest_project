package com.examly.springapp;


import com.examly.springapp.controllers.AppointmentController;
import com.examly.springapp.controllers.DoctorController;
import com.examly.springapp.controllers.PatientController;
import com.examly.springapp.controllers.ReportController;
import com.examly.springapp.entities.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.examly.springapp.entities.Doctor;
import com.examly.springapp.entities.Patient;
import com.examly.springapp.entities.Report;
import com.examly.springapp.repositories.AppointmentRepository;
import com.examly.springapp.repositories.DoctorRepository;
import com.examly.springapp.repositories.PatientRepository;
import com.examly.springapp.services.AppointmentService;
import com.examly.springapp.services.DoctorService;
import com.examly.springapp.services.PatientService;
import com.examly.springapp.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringappApplicationTests {

    @InjectMocks
    private PatientController patientController;

    @InjectMocks
    private DoctorController doctorController;

    @InjectMocks
    private AppointmentController appointmentController;
    @InjectMocks
    private ReportController reportController;

    @Mock
    private PatientService PatientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private com.examly.springapp.repositories.DoctorRepository DoctorRepository;

    @Mock
    private Appointment appointment;
    @Mock
    private ReportService reportService;

    @Mock
    private DoctorService DoctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentService appointmentService;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment1;
    private Appointment appointment2;

    private static final String LOG_FOLDER_PATH = "logs";
    private static final String LOG_FILE_PATH = "logs/application.log";

    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Patient patient1 = new Patient();
    private Patient patient2 = new Patient();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController, doctorController, reportController).build();
    }

    // Test for getting doctor by ID
    @Test
    public void CRUD_testControllerGetDoctor_By_Id() throws Exception {
        Doctor doctor = new Doctor(1L, "Dr. John", "Cardiology", "john.doe@example.com", null);
        when(DoctorService.getDoctor(1L)).thenReturn(doctor);

        mockMvc.perform(get("/doctors/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. John"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.contactInfo").value("john.doe@example.com"));
    }

    // Test for saving a doctor (POST request)
    @Test
    public void CRUD_TestCreateDoctor() throws Exception {
        Doctor doctor = new Doctor(1L, "Dr. Jane", "Neurology", "jane.doe@example.com", null);

        when(DoctorService.saveDoctor(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/doctors")
                .contentType("application/json")
                .content(
                        "{\"name\":\"Dr. Jane\",\"specialization\":\"Neurology\",\"contactInfo\":\"jane.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Jane"))
                .andExpect(jsonPath("$.specialization").value("Neurology"))
                .andExpect(jsonPath("$.contactInfo").value("jane.doe@example.com"));
    }

    // Test for getting all doctors (with pagination)
    @Test
    public void CRUD_TestGetAllDoctors() throws Exception {
        Doctor doctor1 = new Doctor(1L, "Dr. John", "Cardiology", "john.doe@example.com", null);
        Doctor doctor2 = new Doctor(2L, "Dr. Sarah", "Dermatology", "sarah.doe@example.com", null);
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        when(DoctorService.getAllDoctors(0, 10)).thenReturn(new PageImpl<>(doctors));

        mockMvc.perform(get("/doctors")
                .param("page", "0")
                .param("size", "10")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Dr. John"))
                .andExpect(jsonPath("$.content[1].name").value("Dr. Sarah"));
    }

    // Test for deleting a doctor by ID
    @Test
    public void CRUD_TestDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/doctors/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    // _______________________________PATIENT_________________________________________
    @Test
    public void CRUD_TestCreatePatient() throws Exception {
        // Create a patient object for testing
        Patient patient = new Patient(1L, "John Doe", "123-456-7890", null, null);

        // Mock the service to return the same patient object after save
        when(PatientService.savePatient(any(Patient.class))).thenReturn(patient);

        // Perform POST request to save a new patient
        mockMvc.perform(post("/patients")
                .contentType("application/json")
                .content("{\"name\":\"John Doe\",\"contactInfo\":\"123-456-7890\"}"))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created status
                .andExpect(jsonPath("$.name").value("John Doe")) // Expect "name" to be "John Doe"
                .andExpect(jsonPath("$.contactInfo").value("123-456-7890")); // Expect "contactInfo" to be
                                                                             // "123-456-7890"
    }

    @Test
    public void CRUD_TestGetPatientById() throws Exception {
        // Sample patient to retrieve
        Patient patient = new Patient(1L, "John Doe", "123-456-7890", null, null);

        // Mock the service to return the patient
        when(PatientService.getPatient(1L)).thenReturn(patient);

        // Perform GET request to retrieve patient by ID
        mockMvc.perform(get("/patients/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.contactInfo").value("123-456-7890"));
    }

    @Test
    public void CRUD_TestGetAllPatients() throws Exception {
        // Create a list of patients for testing
        List<Patient> patients = List.of(
                new Patient(1L, "John Doe", "123-456-7890", null, null),
                new Patient(2L, "Jane Doe", "234-567-8901", null, null));

        // Mock the service to return a Page of patients
        Page<Patient> patientPage = new PageImpl<>(patients, PageRequest.of(0, 10), patients.size());
        when(PatientService.getAllPatients(0, 10)).thenReturn(patientPage);

        // Perform GET request to retrieve all patients
        mockMvc.perform(get("/patients")
                .param("page", "0")
                .param("size", "10")
                .contentType("application/json"))
                .andExpect(status().isOk())
                // Access content array and check patient names
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Jane Doe"));
    }

    @Test
    public void CRUD_TestDeletePatient() throws Exception {
        // Perform DELETE request to delete a patient
        mockMvc.perform(delete("/patients/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isNoContent()); // Expecting 204 No Content
    }
    // _______________________________________Report___________________________


    @Test
    public void CRUD_TestGetReport() throws Exception {
        Report report = new Report(1L, "Blood Test", "Blood test details", null);

        when(reportService.getReport(1L)).thenReturn(report);

        mockMvc.perform(get("/reports/reports/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportName").value("Blood Test"))
                .andExpect(jsonPath("$.reportDetails").value("Blood test details"));
    }

    // Test DELETE method (Delete Report)
    @Test
    public void CRUD_TestDeleteReport() throws Exception {
        mockMvc.perform(delete("/reports/reports/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ______________________swagger_____________________

    @Test
    public void Swagger_testConfigurationFolder() {
        String directoryPath = "src/main/java/com/examly/springapp/config"; // Replace with the path to your directory
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Test

    public void Swagger_testConfigFile() {

        String filePath = "src/main/java/com/examly/springapp/config/SwaggerConfig.java";

        // Replace with the path to your file

        File file = new File(filePath);

        assertTrue(file.exists() && file.isFile());

    }

    // __________________________AOP________________________
    @Test
    void AOP_testAOPConfigFile() {
        // Path to the LoggingAspect class file
        String filePath = "src/main/java/com/examly/springapp/aspect/LoggingAspect.java";

        // Create a File object
        File file = new File(filePath);

        // Assert that the file exists and is a valid file
        assertTrue(file.exists() && file.isFile(), "LoggingAspect.java file should exist at the specified path.");
    }

    @Test
    void AOP_testAOPConfigFileAspect() throws Exception {
        // Path to the LoggingAspect class file
        Path aspectFilePath = Paths.get("src/main/java/com/examly/springapp/aspect/LoggingAspect.java");

        // Read the content of the aspect file
        String aspectFileContent = Files.readString(aspectFilePath);

        // Check if the file contains @Aspect annotation to ensure it's an Aspect class
        assertTrue(aspectFileContent.contains("@Aspect"), "The LoggingAspect.java should be annotated with @Aspect.");

        // Check if the file contains the logger definition to ensure logging
        // functionality is implemented
        assertTrue(aspectFileContent.contains("private static final Logger logger"),
                "The LoggingAspect.java should define a logger for logging.");
    }

    @Test
    void AOP_testAspectMethods() throws Exception {
        // Path to the LoggingAspect class file
        Path aspectFilePath = Paths.get("src/main/java/com/examly/springapp/aspect/LoggingAspect.java");

        // Read the content of the aspect file
        String aspectFileContent = Files.readString(aspectFilePath);

        // Check for @Before and @AfterReturning annotations to ensure aspect methods
        // are properly defined
        assertTrue(aspectFileContent.contains("@Before"),
                "@Before annotation should be present in the LoggingAspect.java");
        assertTrue(aspectFileContent.contains("@AfterReturning"),
                "@AfterReturning annotation should be present in the LoggingAspect.java");
    }

    @Test
    void AOP_testLoggingStatements() throws Exception {
        // Path to the LoggingAspect class file
        Path aspectFilePath = Paths.get("src/main/java/com/examly/springapp/aspect/LoggingAspect.java");

        // Read the content of the aspect file
        String aspectFileContent = Files.readString(aspectFilePath);

        // Check if logging statements are present in the aspect methods
        assertTrue(aspectFileContent.contains("logger.info"),
                "The LoggingAspect.java should contain logger.info statements for logging.");
    }

    @Test
    void Annotation_testEventHasJSONIgnoreAnnotations() throws Exception {
        // Path to the Event entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Report.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Assert that the file contains @JsonIgnore annotation
        assertTrue(entityFileContent.contains("@JsonIgnore"), "Event Report should contain @JsonIgnore annotation");
    }
    // ______________________PAGINATION__________________________

    @Test
    public void PaginateSorting_testGetPatients() {
        // Create mock patient data
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Doe");

        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(0, 10); // page 0, size 10

        // Create a Page object containing mock patients
        List<Patient> patients = Arrays.asList(patient1, patient2);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());

        // Mock the call to the repository
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);

        // Now call the service method
        Page<Patient> result = PatientService.getAllPatients(0, 10);

        // Print the result to the console (for testing purposes)

    };

    // You can also add assertions here to check the behavior, like:
    // assertEquals(2, result.getContent().size());

    // _____________________JPQL_______________________________

    @Test
    public void JPQL_testFindByPatientId() {
        // Arrange: Create and save a patient
        Patient patient = new Patient();
        patient.setName("John Doe");

        // Ensure the patient is saved and ID is assigned
        patient = patientRepository.save(patient);

        // Assert: Check if the patient is saved properly
        // assertNotNull(patient.getId(), "Patient ID should not be null after save");

        // Arrange: Create and save a doctor
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setSpecialization("Cardiology");
        doctor = doctorRepository.save(doctor);

        // Arrange: Create and save appointments
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentDate(new Date());
        appointment1.setDoctor(doctor);
        appointment1.setPatient(patient);
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentDate(new Date());
        appointment2.setDoctor(doctor);
        appointment2.setPatient(patient);
        appointmentRepository.save(appointment2);

        // Act: Use the repository method to find appointments by patient ID
        // List<Appointment> result =
        // appointmentRepository.findByPatientId(patient.getId());

        // Optionally print the result for debugging
        // System.out.println("Appointments found for patient ID " + patient.getId() +
        // ": " + result.size());

        // Assert: Verify the result contains the correct appointments
        // assertNotNull(result);
        // assertEquals(2, result.size());
    }

    @Test
    public void JPQL_testFindByDoctorId() {
        // Arrange: Create and save a patient
        Patient patient = new Patient();
        patient.setName("Jane Doe");
        patient = patientRepository.save(patient);

        // Arrange: Create and save a doctor
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Adams");
        doctor.setSpecialization("Neurology");
        doctor = doctorRepository.save(doctor);

        // Arrange: Create and save appointments
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentDate(new Date());
        appointment1.setDoctor(doctor);
        appointment1.setPatient(patient);
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentDate(new Date());
        appointment2.setDoctor(doctor);
        appointment2.setPatient(patient);
        appointmentRepository.save(appointment2);

        // Act: Use the repository method to find appointments by doctor ID
        List<Appointment> result = appointmentRepository.findByDoctorId(doctor.getId());

        // Optionally print the result for debugging
        System.out.println("Appointments found for doctor ID " + doctor.getId() + ": " + result.size());

        // Assert: Verify the result contains the correct appointments
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ______________________LOG_____________________
    @Test
    public void LOG_testLogFolderAndFileCreation() {
        String LOG_FOLDER_PATH = "logs";
        String LOG_FILE_PATH = "logs/application.log";
        // Check if the "logs" folder exists
        File logFolder = new File(LOG_FOLDER_PATH);
        assertTrue(logFolder.exists(), "Log folder should be created");

        // Check if the "application.log" file exists inside the "logs" folder
        File logFile = new File(LOG_FILE_PATH);
        assertTrue(logFile.exists(), "Log file should be created inside 'logs' folder");
    }

    // _____________________Repository________________________________
    @Test
    void Repository_testAppointmentRepository() throws Exception {
        // Path to the AppointmentRepository file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/repositories/AppointmentRepository.java");

        // Assert that the file exists
        assertTrue(Files.exists(entityFilePath), "AppointmentRepository file should exist");
    }

    @Test
    void Repository_testDoctorRepository() throws Exception {
        // Path to the DoctorRepository file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/repositories/DoctorRepository.java");

        // Assert that the file exists
        assertTrue(Files.exists(entityFilePath), "DoctorRepository file should exist");
    }

    @Test
    void Repository_testPatientRepository() throws Exception {
        // Path to the PatientRepository file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/repositories/PatientRepository.java");

        // Assert that the file exists
        assertTrue(Files.exists(entityFilePath), "PatientRepository file should exist");
    }

    @Test
    void Repository_testPrescriptionRepository() throws Exception {
        // Path to the PrescriptionRepository file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/repositories/PrescriptionRepository.java");

        // Assert that the file exists
        assertTrue(Files.exists(entityFilePath), "PrescriptionRepository file should exist");
    }

    @Test
    void Repository_testReportRepository() throws Exception {
        // Path to the ReportRepository file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/repositories/ReportRepository.java");

        // Assert that the file exists
        assertTrue(Files.exists(entityFilePath), "ReportRepository file should exist");
    }

    //_________________________Mapping_______________
    @Test
    void Mapping_testAppointmentEntityHasManyToOneRelation() throws Exception {
        // Path to the Appointment entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Appointment.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Check if the @ManyToOne annotation exists in the entity file for both doctor and patient
        assertTrue(entityFileContent.contains("@ManyToOne"), "Appointment entity should contain @ManyToOne annotation");
        assertTrue(entityFileContent.contains("@JoinColumn"), "Appointment entity should contain @JoinColumn annotation");
    }

    @Test
    void Mapping_testDoctorEntityHasOneToManyRelation() throws Exception {
        // Path to the Doctor entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Doctor.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Check if the @OneToMany annotation exists in the entity file
        assertTrue(entityFileContent.contains("@OneToMany"), "Doctor entity should contain @OneToMany annotation");
        assertTrue(entityFileContent.contains("mappedBy = \"doctor\""), "Doctor entity should specify mappedBy for the relation");
    }

    @Test
    void Mapping_testPatientEntityHasOneToManyRelations() throws Exception {
        // Path to the Patient entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Patient.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Check if the @OneToMany annotation exists in the entity file for appointments and prescriptions
        assertTrue(entityFileContent.contains("@OneToMany"), "Patient entity should contain @OneToMany annotation");
        assertTrue(entityFileContent.contains("mappedBy = \"patient\""), "Patient entity should specify mappedBy for the relations");
    }

    @Test
    void Mapping_testPrescriptionEntityHasManyToOneRelation() throws Exception {
        // Path to the Prescription entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Prescription.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Check if the @ManyToOne annotation exists in the entity file
        assertTrue(entityFileContent.contains("@ManyToOne"), "Prescription entity should contain @ManyToOne annotation");
        assertTrue(entityFileContent.contains("@JoinColumn"), "Prescription entity should contain @JoinColumn annotation");
    }
    @Test
    void Mapping_testReportEntityHasManyToOneRelation() throws Exception {
        // Path to the Report entity file
        Path entityFilePath = Paths.get("src/main/java/com/examly/springapp/entities/Report.java");

        // Read the content of the entity file as a string
        String entityFileContent = Files.readString(entityFilePath);

        // Check if the @ManyToOne annotation exists in the entity file
        assertTrue(entityFileContent.contains("@ManyToOne"), "Report entity should contain @ManyToOne annotation");
        assertTrue(entityFileContent.contains("@JoinColumn"), "Report entity should contain @JoinColumn annotation");
    }
}