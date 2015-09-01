package com.sg.startmeup.web.rest;

import com.sg.startmeup.Application;
import com.sg.startmeup.domain.Idea;
import com.sg.startmeup.repository.IdeaRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IdeaResource REST controller.
 *
 * @see IdeaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IdeaResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_CREATION_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_CREATION_DATE = new LocalDate();

    @Inject
    private IdeaRepository ideaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restIdeaMockMvc;

    private Idea idea;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IdeaResource ideaResource = new IdeaResource();
        ReflectionTestUtils.setField(ideaResource, "ideaRepository", ideaRepository);
        this.restIdeaMockMvc = MockMvcBuilders.standaloneSetup(ideaResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        idea = new Idea();
        idea.setName(DEFAULT_NAME);
        idea.setDescription(DEFAULT_DESCRIPTION);
        idea.setCreation_date(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createIdea() throws Exception {
        int databaseSizeBeforeCreate = ideaRepository.findAll().size();

        // Create the Idea

        restIdeaMockMvc.perform(post("/api/ideas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(idea)))
                .andExpect(status().isCreated());

        // Validate the Idea in the database
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeCreate + 1);
        Idea testIdea = ideas.get(ideas.size() - 1);
        assertThat(testIdea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIdea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIdea.getCreation_date()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllIdeas() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

        // Get all the ideas
        restIdeaMockMvc.perform(get("/api/ideas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(idea.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

        // Get the idea
        restIdeaMockMvc.perform(get("/api/ideas/{id}", idea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(idea.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdea() throws Exception {
        // Get the idea
        restIdeaMockMvc.perform(get("/api/ideas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

		int databaseSizeBeforeUpdate = ideaRepository.findAll().size();

        // Update the idea
        idea.setName(UPDATED_NAME);
        idea.setDescription(UPDATED_DESCRIPTION);
        idea.setCreation_date(UPDATED_CREATION_DATE);
        

        restIdeaMockMvc.perform(put("/api/ideas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(idea)))
                .andExpect(status().isOk());

        // Validate the Idea in the database
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeUpdate);
        Idea testIdea = ideas.get(ideas.size() - 1);
        assertThat(testIdea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIdea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIdea.getCreation_date()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void deleteIdea() throws Exception {
        // Initialize the database
        ideaRepository.saveAndFlush(idea);

		int databaseSizeBeforeDelete = ideaRepository.findAll().size();

        // Get the idea
        restIdeaMockMvc.perform(delete("/api/ideas/{id}", idea.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
