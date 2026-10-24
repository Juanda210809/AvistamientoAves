package com.avistamientos.aves.integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.avistamientos.aves.model.Avistamiento;
import com.avistamientos.aves.repository.AvistamientoRepository;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AvistamientoApiIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvistamientoRepository repo;

    // Independencia: cada prueba arranca con la base vacía y prepara sus propios datos
    @BeforeEach
    void limpiarBase() {
        repo.deleteAll();
    }

    private Avistamiento guardar(String especie, String lugar) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setLugar(lugar);
        a.setFecha("2026-10-01");
        a.setObservador("Ana");
        return repo.save(a);
    }

    private String json(String especie, String lugar, String fecha, String observador) {
        return """
                {"especie":"%s","lugar":"%s","fecha":"%s","observador":"%s"}
                """.formatted(especie, lugar, fecha, observador);
    }

    // ---------- GET /avistamientos ----------

    @Test
    void listarDevuelve200ConLosAvistamientosGuardados() throws Exception {
        guardar("colibri", "Chingaza");
        guardar("aguila", "Sumapaz");

        mockMvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].especie").value("colibri"))
                .andExpect(jsonPath("$[1].especie").value("aguila"));
    }

    @Test
    void listarDevuelve200ConListaVaciaCuandoNoHayDatos() throws Exception {
        mockMvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- GET /avistamientos/{id} ----------

    @Test
    void verDevuelve200ConTodosLosCamposDelAvistamiento() throws Exception {
        Avistamiento guardado = guardar("colibri", "Chingaza");

        mockMvc.perform(get("/avistamientos/" + guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(guardado.getId()))
                .andExpect(jsonPath("$.especie").value("colibri"))
                .andExpect(jsonPath("$.lugar").value("Chingaza"))
                .andExpect(jsonPath("$.fecha").value("2026-10-01"))
                .andExpect(jsonPath("$.observador").value("Ana"));
    }

    @Test
    void verDevuelve404CuandoElIdNoExiste() throws Exception {
        mockMvc.perform(get("/avistamientos/999999"))
                .andExpect(status().isNotFound());
    }

    // ---------- POST /avistamientos ----------

    @Test
    void crearDevuelve201YElAvistamientoQuedaConsultablePorSuId() throws Exception {
        String respuesta = mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("colibri", "Chingaza", "2026-10-01", "Ana")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.especie").value("colibri"))
                .andExpect(jsonPath("$.lugar").value("Chingaza"))
                .andReturn().getResponse().getContentAsString();

        long id = ((Number) JsonPath.read(respuesta, "$.id")).longValue();

        // El efecto: un GET posterior devuelve lo que se creó
        mockMvc.perform(get("/avistamientos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observador").value("Ana"));
    }

    @Test
    void crearConEspecieDeDosCaracteresDevuelve400IndicandoElCampoYNoGuarda() throws Exception {
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("ab", "Chingaza", "2026-10-01", "Ana")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("especie"))
                .andExpect(jsonPath("$.error").isNotEmpty());

        // El efecto: no quedó nada guardado
        mockMvc.perform(get("/avistamientos"))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void crearConFechaFuturaDevuelve400IndicandoLaFecha() throws Exception {
        // 2099 sigue siendo futura con el reloj real, así la prueba no caduca
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("colibri", "Chingaza", "2099-01-01", "Ana")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("fecha"));
    }

    @Test
    void crearConCuerpoIncompletoDevuelve400() throws Exception {
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"especie\":\"colibri\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("fecha"));
    }

    // ---------- PUT /avistamientos/{id} ----------

    @Test
    void actualizarDevuelve200YLosCambiosQuedanGuardados() throws Exception {
        Avistamiento guardado = guardar("colibri", "Chingaza");

        mockMvc.perform(put("/avistamientos/" + guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("colibri", "Sumapaz", "2026-10-02", "Ana")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lugar").value("Sumapaz"));

        mockMvc.perform(get("/avistamientos/" + guardado.getId()))
                .andExpect(jsonPath("$.lugar").value("Sumapaz"))
                .andExpect(jsonPath("$.fecha").value("2026-10-02"));
    }

    @Test
    void actualizarDevuelve404CuandoElIdNoExiste() throws Exception {
        mockMvc.perform(put("/avistamientos/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("colibri", "Sumapaz", "2026-10-02", "Ana")))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarConDatosInvalidosDevuelve400YNoModificaElOriginal() throws Exception {
        Avistamiento guardado = guardar("colibri", "Chingaza");

        mockMvc.perform(put("/avistamientos/" + guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("ab", "Sumapaz", "2026-10-02", "Ana")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("especie"));

        mockMvc.perform(get("/avistamientos/" + guardado.getId()))
                .andExpect(jsonPath("$.especie").value("colibri"))
                .andExpect(jsonPath("$.lugar").value("Chingaza"));
    }

    // ---------- DELETE /avistamientos/{id} ----------

    @Test
    void eliminarDevuelve204YDespuesElGetDevuelve404() throws Exception {
        Avistamiento guardado = guardar("colibri", "Chingaza");

        mockMvc.perform(delete("/avistamientos/" + guardado.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/avistamientos/" + guardado.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarDevuelve404CuandoElIdNoExiste() throws Exception {
        mockMvc.perform(delete("/avistamientos/999999"))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /avistamientos/resumen (bonus) ----------

    @Test
    void resumenDevuelve200ConElConteoPorEspecie() throws Exception {
        guardar("colibri", "Chingaza");
        guardar("colibri", "Sumapaz");
        guardar("aguila", "Chingaza");

        mockMvc.perform(get("/avistamientos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.colibri").value(2))
                .andExpect(jsonPath("$.aguila").value(1));
    }
}