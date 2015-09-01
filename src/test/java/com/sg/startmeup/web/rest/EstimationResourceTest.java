package com.sg.startmeup.web.rest;

import com.sg.startmeup.Application;
import com.sg.startmeup.domain.Estimation;
import com.sg.startmeup.repository.EstimationRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EstimationResource REST controller.
 *
 * @see EstimationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EstimationResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final Boolean DEFAULT_PLEDGED = false;
    private static final Boolean UPDATED_PLEDGED = true;

    private static final LocalDate DEFAULT_CREATION_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_CREATION_DATE = new LocalDate();

    @Inject
    private EstimationRepository estimationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEstimationMockMvc;

    private Estimation estimation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstimationResource estimationResource = new EstimationResource();
        ReflectionTestUtils.setField(estimationResource, "estimationRepository", estimationRepository);
        this.restEstimationMockMvc = MockMvcBuilders.standaloneSetup(estimationResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estimation = new Estimation();
        estimation.setName(DEFAULT_NAME);
        estimation.setDescription(DEFAULT_DESCRIPTION);
        estimation.setCost(DEFAULT_COST);
        estimation.setPledged(DEFAULT_PLEDGED);
        estimation.setCreation_date(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createEstimation() throws Exception {
        int databaseSizeBeforeCreate = estimationRepository.findAll().size();

        // Create the Estimation

        restEstimationMockMvc.perform(post("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimation)))
                .andExpect(status().isCreated());

        // Validate the Estimation in the database
        List<Estimation> estimations = estimationRepository.findAll();
        assertThat(estimations).hasSize(databaseSizeBeforeCreate + 1);
        Estimation testEstimation = estimations.get(estimations.size() - 1);
        assertThat(testEstimation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEstimation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEstimation.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testEstimation.getPledged()).isEqualTo(DEFAULT_PLEDGED);
        assertThat(testEstimation.getCreation_date()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllEstimations() throws Exception {
        // Initialize the database
        estimationRepository.saveAndFlush(estimation);

        // Get all the estimations
        restEstimationMockMvc.perform(get("/api/estimations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estimation.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
                .andExpect(jsonPath("$.[*].pledged").value(hasItem(DEFAULT_PLEDGED.booleanValue())))
                .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEstimation() throws Exception {
        // Initialize the database
        estimationRepository.saveAndFlush(estimation);

        // Get the estimation
        restEstimationMockMvc.perform(get("/api/estimations/{id}", estimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estimation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.pledged").value(DEFAULT_PLEDGED.booleanValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstimation() throws Exception {
        // Get the estimation
        restEstimationMockMvc.perform(get("/api/estimations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstimation() throws Exception {
        // Initialize the database
        estimationRepository.saveAndFlush(estimation);

		int databaseSizeBeforeUpdate = estimationRepository.findAll().size();

        // Update the estimation
        estimation.setName(UPDATED_NAME);
        estimation.setDescription(UPDATED_DESCRIPTION);
        estimation.setCost(UPDATED_COST);
        estimation.setPledged(UPDATED_PLEDGED);
        estimation.setCreation_date(UPDATED_CREATION_DATE);
        

        restEstimationMockMvc.perform(put("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimation)))
                .andExpect(status().isOk());

        // Validate the Estimation in the database
        List<Estimation> estimations = estimationRepository.findAll();
        assertThat(estimations).hasSize(databaseSizeBeforeUpdate);
        Estimation testEstimation = estimations.get(estimations.size() - 1);
        assertThat(testEstimation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEstimation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEstimation.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testEstimation.getPledged()).isEqualTo(UPDATED_PLEDGED);
        assertThat(testEstimation.getCreation_date()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void deleteEstimation() throws Exception {
        // Initialize the database
        estimationRepository.saveAndFlush(estimation);

		int databaseSizeBeforeDelete = estimationRepository.findAll().size();

        // Get the estimation
        restEstimationMockMvc.perform(delete("/api/estimations/{id}", estimation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estimation> estimations = estimationRepository.findAll();
        assertThat(estimations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
