package org.example.kotlinconference101

/** How a project slots into the quest log. Hackathons are BOSS fights. */
enum class QuestType(val label: String) {
    MAIN("MAIN QUEST"),
    SIDE("SIDE QUEST"),
    BOSS("BOSS FIGHT")
}

enum class QuestRank(val label: String) { S("S"), A("A"), B("B") }

enum class QuestStatus(val label: String) {
    CLEARED("CLEARED"),
    IN_PROGRESS("IN PROGRESS"),
    BOSS_CLEARED("BOSS CLEARED")
}

data class Project(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String = "", // Empty for now, can be filled with resources
    val technologies: List<String>,
    val features: List<String>,
    val githubUrl: String = "",
    val liveUrl: String = "",
    val startDate: String,
    val endDate: String? = null,
    val featured: Boolean = false,
    // Quest-log fields
    val rank: QuestRank = QuestRank.B,
    val xp: Int = 500,
    val status: QuestStatus = QuestStatus.IN_PROGRESS,
    val progress: Float = 0f, // 0..1 completion shown on the segmented bar
    val questType: QuestType = QuestType.SIDE
)

/**
 * All quest-log UI copy lives here (not in composables) so quests and
 * labels can be edited without touching UI code.
 */
object QuestLogCopy {
    const val TITLE = "QUEST LOG"
    const val SUBTITLE = "Explore my recent work and achievements"
    const val LEVEL_PREFIX = "LVL"
    const val XP_SUFFIX = "XP"
    const val FILTER_PREFIX = "🎲"
    const val CLEAR_FILTER = "CLEAR"
    const val EMPTY_FILTER = "No quests match this filter yet."
    const val BOSS_TROPHY = "🏆"
    const val BOSS_TOAST = "YOU DID IT!"
    const val BACK = "◀ BACK"
    const val BRIEFING = "MISSION BRIEFING"
    const val OBJECTIVE = "OBJECTIVE"
    const val TACTICS = "TACTICS"
    const val LOOT = "LOOT ACQUIRED"
    const val GITHUB_LINK = "GITHUB"
    const val LIVE_LINK = "LIVE DEMO"
    const val GITHUB_EMOJI = "⌨"
    const val LIVE_EMOJI = "🌐"
    const val TACTIC_BULLET = "▸"
    const val DATE_ONGOING_PREFIX = "Starting"
}

data class Experience(
    val id: String,
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String? = null,
    val description: String,
    val skills: List<String>,
    val isCurrent: Boolean = false
)

data class Skill(
    val name: String,
    val proficiency: Int, // 1-5 scale
    val category: String // e.g., "Language", "Framework", "Tool"
)

data class Contact(
    val email: String,
    val linkedIn: String,
    val github: String,
    val twitter: String = "",
    val buyMeACoffee: String = "",
    val githubSponsors: String = "",
    val calCom: String = ""
)

object PortfolioDataProvider {
    val projects = listOf(
        Project(
            id = "1",
            title = "GKash",
            description = "An AI-powered mobile app teaching saving and investing to underserved communities in Kenya. 2nd place winner in the 2025 Absa GirlCodeHack Pan-African women-in-tech competition.",
            technologies = listOf("Kotlin", "Jetpack Compose"),
            features = listOf(
                "AI-powered financial guidance",
                "Saving and investment education",
                "Community-focused approach",
                "Mobile-first design",
                "Accelerator accepted"
            ),
            githubUrl = "https://github.com/clencyc/gkash",
            liveUrl = "https://gkash-gilt.vercel.app/",
            startDate = "2025-01",
            endDate = null,
            featured = true,
            rank = QuestRank.S,
            xp = 2500,
            status = QuestStatus.BOSS_CLEARED,
            progress = 0.85f,
            questType = QuestType.BOSS
        ),
        Project(
            id = "2",
            title = "TechiPro Konnect",
            description = "A community-driven platform for connecting tech professionals and opportunities in Africa.",
            technologies = listOf("Kotlin", "Jetpack Compose", "XML", "PostgreSQL"),
            features = listOf(
                "Professional networking",
                "Opportunity discovery",
                "Community engagement",
                "Real-time collaboration",
                "Resource sharing"
            ),
            githubUrl = "https://github.com/clencyc/techiprokonnect",
            startDate = "2024-06",
            endDate = null,
            featured = true,
            rank = QuestRank.A,
            xp = 1800,
            status = QuestStatus.IN_PROGRESS,
            progress = 0.65f,
            questType = QuestType.MAIN
        ),
        Project(
            id = "3",
            title = "HakiChain",
            description = "A blockchain-based application for secure and transparent data management and verification.",
            technologies = listOf("Python","Web scraping" ),
            features = listOf(
                "Secure data verification",
                "Blockchain integration",
                "Transparency and immutability",
                "Decentralized architecture",
                "Audit trails"
            ),
            startDate = "2025-06",
            endDate = "2025-11",
            rank = QuestRank.B,
            xp = 900,
            status = QuestStatus.CLEARED,
            progress = 1f,
            questType = QuestType.SIDE
        ),
        Project(
            id = "4",
            title = "Live Edit",
            description = "A simple web app I built for real-time collaborative editing, so people can write and edit documents together and see each other's changes as they happen.",
            technologies = listOf("JavaScript", "Web Sockets"),
            features = listOf(
                "Real-time collaborative editing",
                "Live cursor and change tracking",
                "Simple, no-clutter interface"
            ),
            liveUrl = "https://livedit.space",
            startDate = "2025-05",
            endDate = null,
            rank = QuestRank.B,
            xp = 700,
            status = QuestStatus.CLEARED,
            progress = 1f,
            questType = QuestType.SIDE
        ),
        Project(
            id = "5",
            title = "ProcureGuard AI",
            description = "A fraud detection and procurement monitoring system using machine learning to identify suspicious patterns and protect organizations.",
            technologies = listOf("Python", "Machine Learning", "Django", "PostgreSQL"),
            features = listOf(
                "Real-time fraud detection",
                "ML-powered anomaly detection",
                "Procurement monitoring",
                "Risk assessment",
                "Automated reporting"
            ),
            startDate = "2024-06",
            endDate = null,
            rank = QuestRank.A,
            xp = 1200,
            status = QuestStatus.IN_PROGRESS,
            progress = 0.5f,
            questType = QuestType.MAIN
        )
    )

    /** XP from quests that are no longer in progress — fills the header bar. */
    val earnedQuestXp: Int get() = projects.filter { it.status != QuestStatus.IN_PROGRESS }.sumOf { it.xp }
    val totalQuestXp: Int get() = projects.sumOf { it.xp }
    val playerLevel: Int get() = earnedQuestXp / 1000 + 1

    val experiences = listOf(
        Experience(
            id = "1",
            title = "Google Developer Group on Campus Organizer",
            company = "GDG On Campus JKUAT",
            startDate = "2025-09",
            endDate = null,
            description = "Organizing events, sessions and partnerships for the Google Developer Group community at Jomo Kenyatta University of Agriculture and Technology.",
            skills = listOf("Community Building", "Event Management", "Networking", "Leadership", "Technical Mentoring"),
            isCurrent = true
        ),
        Experience(
            id = "2",
            title = "Django Backend Developer",
            company = "Tech For Nonprofits",
            startDate = "2025-06",
            endDate = null,
            description = "Building and maintaining scalable REST APIs using Django & Django REST Framework. Designing and optimizing database models, implementing authentication and security best practices, and collaborating with frontend developers and stakeholders.",
            skills = listOf("Django", "Django REST Framework", "PostgreSQL", "REST APIs", "Python", "Security", "Code Reviews"),
            isCurrent = true
        ),
        Experience(
            id = "3",
            title = "Co-Founder & Android Developer",
            company = "TechiPro Konnect App",
            startDate = "2024-06",
            endDate = null,
            description = "Co-founding and developing an Android application for connecting tech professionals in Africa. Managing product development and building features with Kotlin and Jetpack Compose.",
            skills = listOf("Kotlin", "Jetpack Compose", "Android Development", "Product Management", "Entrepreneurship"),
            isCurrent = true
        ),
        Experience(
            id = "4",
            title = "Software Developer",
            company = "HakiChain",
            startDate = "2025-06",
            endDate = "2025-11",
            description = "Contributed to blockchain-based application development with focus on secure data verification and transparent management systems.",
            skills = listOf("Blockchain", "Python", "Kotlin", "Smart Contracts", "Web3"),
            isCurrent = false
        ),
        Experience(
            id = "5",
            title = "ALX Software Engineering Graduate",
            company = "ALX Software Engineering",
            startDate = "2023-01",
            endDate = "2024-12",
            description = "Completed intensive software engineering program covering full-stack development, data structures, algorithms, and system design.",
            skills = listOf("Full-Stack Development", "Data Structures", "Algorithms", "System Design", "Software Engineering Fundamentals"),
            isCurrent = false
        )
    )

    val skills = listOf(
        // Languages
        Skill("Python", 5, "Language"),
        Skill("Kotlin", 5, "Language"),
        Skill("Java", 4, "Language"),
        Skill("JavaScript", 3, "Language"),
        Skill("SQL", 4, "Language"),
        
        // Backend & Frameworks
        Skill("Django", 5, "Framework"),
        Skill("Django REST Framework", 5, "Framework"),
        Skill("FastAPI", 4, "Framework"),
        Skill("REST APIs", 5, "Framework"),
        Skill("Jetpack Compose", 5, "Framework"),
        Skill("Android", 5, "Framework"),
        
        // AI & ML
        Skill("Machine Learning", 4, "AI"),
        Skill("PyTorch", 3, "AI"),
        Skill("NLP", 3, "AI"),
        Skill("Applied AI", 4, "AI"),
        
        // Database & Tools
        Skill("PostgreSQL", 4, "Database"),
        Skill("SQLite", 4, "Database"),
        Skill("Room Database", 4, "Database"),
        Skill("Git", 5, "Tool"),
        Skill("Gradle", 4, "Tool"),
        Skill("Docker", 3, "Tool"),
        
        // Soft Skills
        Skill("Working under Pressure", 5, "Soft Skill"),
        Skill("Project Planning", 5, "Soft Skill"),
        Skill("Pitching", 5, "Soft Skill"),
        Skill("Leadership", 4, "Soft Skill"),
        Skill("Team Collaboration", 5, "Soft Skill"),
        
        // Blockchain & Web3
        Skill("Blockchain", 3, "Technology"),
        Skill("Smart Contracts", 3, "Technology")
    )

    val contact = Contact(
        email = "christineoyiera51@gmail.com",
        linkedIn = "https://www.linkedin.com/in/clency-christine-643b32265",
        github = "https://github.com/clencyc",
        twitter = "",
        buyMeACoffee = "https://buymeacoffee.com/clencyc",
        githubSponsors = "https://github.com/sponsors/clencyc",
        calCom = "https://cal.com/christine-oyiera-ngz2gj"
    )

    val bio = """
        Hey, I'm Clency! I love building and experimenting with stuff. I'm not an expert by any means, but
        I enjoy learning by doing — I pick a project, dive in, and figure things out along the way.

        Based in Nairobi County, Kenya, I mostly tinker with Python and Django on the backend, and Kotlin with
        Jetpack Compose for Android. I'm also curious about how AI works and I'm slowly teaching myself machine
        learning through small projects and a lot of trial and error.

        As part of an all-women team, I co-built GKash, which won 2nd place at the 2025 Absa GirlCodeHack and
        got accepted into an accelerator — still can't quite believe that happened! I like turning ideas into
        working apps, even the messy, imperfect ones, because that's how I learn best.
    """.trimIndent()

    val funFact = "2nd Place Winner at 2025 Absa GirlCodeHack Pan-African Women-in-Tech Competition with GKash AI App!"
}
