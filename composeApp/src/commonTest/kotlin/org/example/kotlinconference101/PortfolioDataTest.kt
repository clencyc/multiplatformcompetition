package org.example.kotlinconference101

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PortfolioDataTest {
    
    @Test
    fun testProjectsNotEmpty() {
        val projects = PortfolioDataProvider.projects
        assertTrue(projects.isNotEmpty(), "Projects list should not be empty")
        assertEquals(5, projects.size, "Should have 5 projects")
    }

    @Test
    fun testFeaturedProjectExists() {
        val projects = PortfolioDataProvider.projects
        val featuredProjects = projects.filter { it.featured }
        assertTrue(featuredProjects.isNotEmpty(), "Should have at least one featured project")
    }

    @Test
    fun testProjectHasTechnologies() {
        val project = PortfolioDataProvider.projects.first()
        assertTrue(project.technologies.isNotEmpty(), "Project should have technologies")
    }

    @Test
    fun testExperiencesNotEmpty() {
        val experiences = PortfolioDataProvider.experiences
        assertTrue(experiences.isNotEmpty(), "Experiences list should not be empty")
        assertEquals(3, experiences.size, "Should have 3 experiences")
    }

    @Test
    fun testCurrentExperienceExists() {
        val experiences = PortfolioDataProvider.experiences
        val currentExp = experiences.find { it.isCurrent }
        assertNotNull(currentExp, "Should have a current experience")
    }

    @Test
    fun testSkillsNotEmpty() {
        val skills = PortfolioDataProvider.skills
        assertTrue(skills.isNotEmpty(), "Skills list should not be empty")
    }

    @Test
    fun testSkillsHaveCategories() {
        val skills = PortfolioDataProvider.skills
        val categories = skills.map { it.category }.distinct()
        assertTrue(categories.isNotEmpty(), "Skills should have categories")
    }

    @Test
    fun testContactInfoComplete() {
        val contact = PortfolioDataProvider.contact
        assertFalse(contact.email.isEmpty(), "Email should not be empty")
        assertFalse(contact.linkedIn.isEmpty(), "LinkedIn should not be empty")
        assertFalse(contact.github.isEmpty(), "GitHub should not be empty")
    }

    @Test
    fun testBioNotEmpty() {
        val bio = PortfolioDataProvider.bio
        assertFalse(bio.isEmpty(), "Bio should not be empty")
    }

    @Test
    fun testProjectDataIntegrity() {
        val project = PortfolioDataProvider.projects.first()
        assertEquals("1", project.id, "First project ID should be 1")
        assertFalse(project.title.isEmpty(), "Project title should not be empty")
        assertFalse(project.description.isEmpty(), "Project description should not be empty")
    }

    @Test
    fun testSkillsProficiencyRange() {
        val skills = PortfolioDataProvider.skills
        skills.forEach { skill ->
            assertTrue(
                skill.proficiency in 1..5,
                "Skill proficiency should be between 1 and 5, got ${skill.proficiency} for ${skill.name}"
            )
        }
    }

    @Test
    fun testNavigationItemsCount() {
        val items = NavigationItem.entries
        assertEquals(3, items.size, "Should have exactly 3 navigation items")
    }
}
