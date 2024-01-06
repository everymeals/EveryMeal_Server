package everymeal.server.report.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.global.util.authresolver.UserJwtResolver;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.report.dto.ReportDto;
import everymeal.server.report.dto.ReportDto.ReportReviewReq;
import everymeal.server.report.entity.ReportReason;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class ReportControllerTest extends ControllerTestSupport {

    @MockBean UserJwtResolver userJwtResolver;

    @Test
    void 식당리뷰_신고하기() throws Exception {
        // given
        ReportDto.ReportReviewReq req = new ReportReviewReq(ReportReason.UNRELATED);

        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when-then
        mockMvc.perform(
                        post("/api/v1/reports/review/{reveiwIdx}", 1L)
                                .content(objectMapper.writeValueAsString(req))
                                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 식당리뷰_신고하기_신고이유_없음() throws Exception {
        // given
        given(userJwtResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedUser.builder().idx(1L).build());

        // when-then
        mockMvc.perform(
                        post("/api/v1/reports/review/{reveiwIdx}", 1L)
                                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
