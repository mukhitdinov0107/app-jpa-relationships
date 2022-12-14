package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    AddressRepository addressRepository;
    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentsListsForFaculty(@PathVariable Integer facultyId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }
    //3. FACULTY DEKANAT
    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentsForGroup(@PathVariable Integer groupId,
                                             @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(groupId, pageable);
        return studentPage;
    }
    @PostMapping String addStudent(@RequestBody StudentDto studentDto){
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return  "Group doesn't exists";
        }
        else {

            Address address = new Address();
            address.setCity(studentDto.getCity());
            address.setDistrict(studentDto.getDistrict());
            address.setStreet(studentDto.getStreet());
            Address savedAddress = addressRepository.save(address);
            student.setGroup(optionalGroup.get());
            student.setAddress(savedAddress);
            studentRepository.save(student);
            return "saved";
        }

    }
    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            Student student = optionalStudent.get();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());

            Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
            if (!optionalGroup.isPresent()) {
                return "Group doesn't exists";
            }
            student.setGroup(optionalGroup.get());
            Address address = student.getAddress();
            address.setCity(studentDto.getCity());
            address.setDistrict(studentDto.getDistrict());
            address.setStreet(studentDto.getStreet());
            addressRepository.save(address);
            studentRepository.save(student);
        }
        return "university edited";
    }
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        try {
            studentRepository.deleteById(id);
            return "Student deleted";
        } catch (Exception e) {
            return "Error in deleting";
        }
    }

}
