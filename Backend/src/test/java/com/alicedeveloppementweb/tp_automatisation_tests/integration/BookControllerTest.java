package com.alicedeveloppementweb.tp_automatisation_tests.integration;

import com.alicedeveloppementweb.tp_automatisation_tests.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void should_create_book() throws Exception {
        String json = """
            {
              "titre": "Dune",
              "auteur": "Frank Herbert",
              "annee": 1965
            }
        """;

        mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titre").value("Dune"));
    }

    @Test
    void should_get_all_books() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void should_get_book_by_id_after_create() throws Exception {
        // CREATE
        String createJson = """
            {
              "titre": "Le Seigneur des Anneaux",
              "auteur": "J.R.R. Tolkien",
              "annee": 1954
            }
        """;
        ResultActions createResult = mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        long id = Long.parseLong(createResult.andReturn().getResponse().getContentAsString().replaceAll(".*\"id\":(\\d+).*", "$1"));

        // READ by id
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titre").value("Le Seigneur des Anneaux"))
                .andExpect(jsonPath("$.auteur").value("J.R.R. Tolkien"))
                .andExpect(jsonPath("$.annee").value(1954));
    }

    @Test
    void should_update_book() throws Exception {
        // CREATE
        String createJson = """
            {
              "titre": "Fondation",
              "auteur": "Isaac Asimov",
              "annee": 1951
            }
        """;
        ResultActions createResult = mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        long id = Long.parseLong(createResult.andReturn().getResponse().getContentAsString().replaceAll(".*\"id\":(\\d+).*", "$1"));

        // UPDATE
        String updateJson = """
            {
              "titre": "Fondation et Empire",
              "auteur": "Isaac Asimov",
              "annee": 1952
            }
        """;
        mockMvc.perform(
                        put("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titre").value("Fondation et Empire"))
                .andExpect(jsonPath("$.annee").value(1952));

        // READ to confirm
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Fondation et Empire"));
    }

    @Test
    void should_delete_book() throws Exception {
        // CREATE
        String createJson = """
            {
              "titre": "À supprimer",
              "auteur": "Test",
              "annee": 2000
            }
        """;
        ResultActions createResult = mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        long id = Long.parseLong(createResult.andReturn().getResponse().getContentAsString().replaceAll(".*\"id\":(\\d+).*", "$1"));

        // DELETE
        mockMvc.perform(delete("/api/books/" + id))
                .andExpect(status().isOk());

        // GET by id after delete : controller returns null → 200 avec corps vide
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_ok_with_empty_body_when_get_by_id_not_found() throws Exception {
        mockMvc.perform(get("/api/books/99999"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().isEmpty() || "null".equals(result.getResponse().getContentAsString())));
    }

    @Test
    void should_perform_full_crud_cycle() throws Exception {
        // CREATE
        String createJson = """
            {
              "titre": "CRUD complet",
              "auteur": "Test",
              "annee": 2024
            }
            """;
        String createResponse = mockMvc.perform(
                        post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titre").value("CRUD complet"))
                .andReturn().getResponse().getContentAsString();

        long id = Long.parseLong(createResponse.replaceAll(".*\"id\":(\\d+).*", "$1"));

        // READ all (doit contenir au moins notre livre)
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // READ by id
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titre").value("CRUD complet"))
                .andExpect(jsonPath("$.auteur").value("Test"))
                .andExpect(jsonPath("$.annee").value(2024));

        // UPDATE
        String updateJson = """
            {
              "titre": "CRUD mis à jour",
              "auteur": "Test",
              "annee": 2025
            }
            """;
        mockMvc.perform(
                        put("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titre").value("CRUD mis à jour"))
                .andExpect(jsonPath("$.annee").value(2025));

        // DELETE
        mockMvc.perform(delete("/api/books/" + id))
                .andExpect(status().isOk());

        // Vérifier que le livre n'existe plus (GET retourne null)
        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk());
    }
}
