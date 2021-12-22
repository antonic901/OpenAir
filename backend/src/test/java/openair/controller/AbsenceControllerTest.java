package openair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import openair.model.Absence;
import openair.model.enums.Status;
import openair.security.auth.RestAuthenticationEntryPoint;
import openair.service.AbsenceService;
import openair.service.EmployeeService;
import openair.service.UserService;
import openair.utils.TokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AbsenceController.class)
class AbsenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AbsenceService absenceService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @MockBean
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @MockBean
    private TokenUtils tokenUtils;

    @Test
    void whenValidInput_thenReturn204() throws Exception {

        given(absenceService.review(any(Long.class), any(Status.class))).willReturn(new Absence());

        MvcResult result = mockMvc.perform(put("/absences/1")
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Status.APPROVED)))
                .andExpect(status().isNoContent()).andReturn();

            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isNoContent()).andExpect(forwardedUrl("default")).andReturn();

//        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
