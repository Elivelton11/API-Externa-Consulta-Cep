package com.example.demo;


import com.example.demo.DTO.ApplicationCepResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import static java.lang.reflect.Array.get;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class ApiConsultaCepApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testCreateSucess() throws Exception{
        String cepValido = "88801000";
        String urlApi = String.format("http://viacep.com.br/ws/%s/json", cepValido);

        ApplicationCepResultDTO mockResult = new ApplicationCepResultDTO(
                "8880100","Avenida Centenario","Até 9999 - lado ímpar","Centro","Criciúma","SC");
        when(restTemplate.getForObject(urlApi,ApplicationCepResultDTO.class)).thenReturn(mockResult);

        mockMvc.perform(get("/consulta-cep").param("cep",cepValido)).andExpect(status().isOk()).andExpect(content().string(containsString("Criciúma")))
                .andExpect(content().string(containsString("Rua Ararangua")));

    }
    @Test
    void testCreateFailure() throws Exception{
        String cepInvalido = "99999999";
        String urlApi = String.format("http://viacep.com.br/ws/%s/json", cepInvalido);

        when(restTemplate.getForObject(urlApi,ApplicationCepResultDTO.class)).thenThrow(new RestClientException("Cep não encontrado"));
        mockMvc.perform(get("/consulta-cep").param("cep", cepInvalido)).andExpect(status().isOk()).andExpect(content().string(contains("CEP não encontrado")));
    }
}
