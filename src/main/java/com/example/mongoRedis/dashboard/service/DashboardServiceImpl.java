package com.example.mongoRedis.dashboard.service;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.dashboard.dto.response.*;
import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        log.debug("Generating admin dashboard");
        
        // Get user statistics
        long totalStudents = userRepository.countByUserType(UserType.STUDENT);
        long totalTeachers = userRepository.countByUserType(UserType.TEACHER);
        long totalStaff = userRepository.countByUserType(UserType.STAFF);
        
        // Mock data for now - replace with actual data from your database
        return AdminDashboardResponse.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalStaff(totalStaff)
                .totalFeesCollected(new BigDecimal("1250000.00"))
                .pendingFees(new BigDecimal("85000.00"))
                .pendingApplications(15)
                .activeCourses(24)
                .totalCourses(30)
                .recentUsers(getRecentUsers())
                .recentApplications(getRecentApplications())
                .popularCourses(getPopularCourses())
                .financialSummary(getFinancialSummary())
                .build();
    }

    @Override
    public TeacherDashboardResponse getTeacherDashboard() {
        log.debug("Generating teacher dashboard");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String teacherId = auth.getName(); // This will be the user ID
        
        // Mock data for now - replace with actual data
        return TeacherDashboardResponse.builder()
                .teacherName("John Doe")
                .employeeId("EMP001")
                .department("Mathematics")
                .totalStudents(85)
                .totalCourses(4)
                .pendingApplications(3)
                .todayClasses(3)
                .upcomingClasses(2)
                .assignedCourses(getTeacherCourses())
                .recentStudents(getRecentStudents())
//                .pendingApplications(getTeacherApplications())
                .todaySchedule(getTodaySchedule())
                .notifications(getTeacherNotifications())
                .build();
    }

    @Override
    public StudentDashboardResponse getStudentDashboard() {
        log.debug("Generating student dashboard");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = auth.getName(); // This will be the user ID
        
        // Mock data for now - replace with actual data
        return StudentDashboardResponse.builder()
                .studentName("Alice Johnson")
                .studentId("STU2024001")
                .grade("10")
                .section("A")
                .academicYear("2024-2025")
                .currentCourses(getStudentCourses())
                .grades(getStudentGrades())
                .attendance(getStudentAttendance())
                .feeStatus(getStudentFeeStatus())
                .applications(getStudentApplications())
                .notifications(getStudentNotifications())
                .announcements(getStudentAnnouncements())
                .build();
    }

    // Mock data methods - replace with actual database queries
    private List<AdminDashboardResponse.UserSummary> getRecentUsers() {
        return Arrays.asList(
            AdminDashboardResponse.UserSummary.builder()
                .id("1").name("Alice Johnson").email("alice@email.com")
                .userType("STUDENT").createdAt("2024-01-15").isActive(true).build(),
            AdminDashboardResponse.UserSummary.builder()
                .id("2").name("Bob Smith").email("bob@email.com")
                .userType("TEACHER").createdAt("2024-01-14").isActive(true).build()
        );
    }

    private List<AdminDashboardResponse.ApplicationSummary> getRecentApplications() {
        return Arrays.asList(
            AdminDashboardResponse.ApplicationSummary.builder()
                .id("1").studentName("Alice Johnson").type("LEAVE")
                .status("PENDING").submittedAt("2024-01-15").build()
        );
    }

    private List<AdminDashboardResponse.CourseSummary> getPopularCourses() {
        return Arrays.asList(
            AdminDashboardResponse.CourseSummary.builder()
                .id("1").name("Mathematics").code("MATH101")
                .enrolledStudents(45).teacherName("John Doe").build()
        );
    }

    private AdminDashboardResponse.FinancialSummary getFinancialSummary() {
        return AdminDashboardResponse.FinancialSummary.builder()
            .thisMonth(new BigDecimal("125000.00"))
            .lastMonth(new BigDecimal("118000.00"))
            .thisYear(new BigDecimal("1250000.00"))
            .paymentCount(245).build();
    }

    private List<TeacherDashboardResponse.CourseSummary> getTeacherCourses() {
        return Arrays.asList(
            TeacherDashboardResponse.CourseSummary.builder()
                .id("1").name("Mathematics").code("MATH101")
                .enrolledStudents(25).grade("10").section("A")
                .schedule("Mon, Wed, Fri - 9:00 AM").build()
        );
    }

    private List<TeacherDashboardResponse.StudentSummary> getRecentStudents() {
        return Arrays.asList(
            TeacherDashboardResponse.StudentSummary.builder()
                .id("1").name("Alice Johnson").studentId("STU001")
                .grade("10").section("A").averageGrade(85.5).attendanceRate(92).build()
        );
    }

    private List<TeacherDashboardResponse.ApplicationSummary> getTeacherApplications() {
        return Arrays.asList(
            TeacherDashboardResponse.ApplicationSummary.builder()
                .id("1").studentName("Alice Johnson").type("LEAVE")
                .submittedAt("2024-01-15").priority("NORMAL").build()
        );
    }

    private List<TeacherDashboardResponse.ClassSummary> getTodaySchedule() {
        return Arrays.asList(
            TeacherDashboardResponse.ClassSummary.builder()
                .courseId("1").courseName("Mathematics")
                .time("9:00 AM - 10:00 AM").room("101")
                .grade("10").section("A").build()
        );
    }

    private TeacherDashboardResponse.NotificationSummary getTeacherNotifications() {
        return TeacherDashboardResponse.NotificationSummary.builder()
            .total(5).unread(2)
            .recent(Arrays.asList(
                TeacherDashboardResponse.NotificationItem.builder()
                    .id("1").message("New leave application from Alice")
                    .type("APPLICATION").createdAt("2024-01-15").isRead(false).build()
            )).build();
    }

    private List<StudentDashboardResponse.CourseSummary> getStudentCourses() {
        return Arrays.asList(
            StudentDashboardResponse.CourseSummary.builder()
                .id("1").name("Mathematics").code("MATH101")
                .teacherName("John Doe").schedule("Mon, Wed, Fri - 9:00 AM")
                .room("101").currentGrade(85.5).attendanceRate(92).build()
        );
    }

    private StudentDashboardResponse.GradeSummary getStudentGrades() {
        return StudentDashboardResponse.GradeSummary.builder()
            .overallGPA(3.5).thisSemesterGPA(3.6)
            .totalSubjects(6).passedSubjects(5).failedSubjects(1)
            .subjectGrades(Arrays.asList(
                StudentDashboardResponse.SubjectGrade.builder()
                    .subjectName("Mathematics").grade(85.5)
                    .status("PASS").teacherName("John Doe").build()
            )).build();
    }

    private StudentDashboardResponse.AttendanceSummary getStudentAttendance() {
        return StudentDashboardResponse.AttendanceSummary.builder()
            .totalDays(120).presentDays(110).absentDays(8)
            .lateDays(2).attendanceRate(91.7).consecutiveAbsent(0).build();
    }

    private StudentDashboardResponse.FeeSummary getStudentFeeStatus() {
        return StudentDashboardResponse.FeeSummary.builder()
            .totalFees(new BigDecimal("50000.00"))
            .paidFees(new BigDecimal("45000.00"))
            .pendingFees(new BigDecimal("5000.00"))
            .overdueFees(BigDecimal.ZERO)
            .nextPaymentDue("2024-02-01")
            .paymentHistory(Arrays.asList(
                StudentDashboardResponse.PaymentRecord.builder()
                    .id("1").amount(new BigDecimal("45000.00"))
                    .paymentDate("2024-01-01").method("BANK_TRANSFER")
                    .status("PAID").build()
            )).build();
    }

    private List<StudentDashboardResponse.ApplicationSummary> getStudentApplications() {
        return Arrays.asList(
            StudentDashboardResponse.ApplicationSummary.builder()
                .id("1").type("LEAVE").status("APPROVED")
                .submittedAt("2024-01-10").lastUpdated("2024-01-11")
                .response("Approved by teacher").build()
        );
    }

    private StudentDashboardResponse.NotificationSummary getStudentNotifications() {
        return StudentDashboardResponse.NotificationSummary.builder()
            .total(3).unread(1)
            .recent(Arrays.asList(
                StudentDashboardResponse.NotificationItem.builder()
                    .id("1").message("Mathematics test scheduled for next week")
                    .type("ACADEMIC").createdAt("2024-01-15").isRead(false).build()
            )).build();
    }

    private List<StudentDashboardResponse.AnnouncementSummary> getStudentAnnouncements() {
        return Arrays.asList(
            StudentDashboardResponse.AnnouncementSummary.builder()
                .id("1").title("Mid-term Examination Schedule")
                .message("Mid-term exams will start from next Monday")
                .postedBy("Principal").postedAt("2024-01-15")
                .priority("HIGH").build()
        );
    }
}
