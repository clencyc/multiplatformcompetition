package org.example.kotlinconference101

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
    val featured: Boolean = false
)

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
    val twitter: String = ""
)

object PortfolioDataProvider {
    val projects = listOf(
        Project(
            id = "1",
            title = "Multiplatform Portfolio App",
            description = "A beautiful cross-platform portfolio application built with Jetpack Compose Multiplatform, showcasing Android, Desktop, and Web support.",
            technologies = listOf("Kotlin", "Compose Multiplatform", "Material 3", "Coroutines"),
            features = listOf(
                "Cross-platform UI with single codebase",
                "Material 3 theming with light/dark mode",
                "Responsive layouts for mobile and desktop",
                "Smooth animations and transitions",
                "Offline-first architecture"
            ),
            githubUrl = "https://github.com/yourusername/portfolio",
            liveUrl = "https://portfolio.example.com",
            startDate = "2024-10",
            endDate = null,
            featured = true
        ),
        Project(
            id = "2",
            title = "Real-time Chat Application",
            description = "A full-stack chat app with real-time messaging, user authentication, and media sharing capabilities.",
            technologies = listOf("Kotlin", "Firebase", "Jetpack Compose", "WebSocket"),
            features = listOf(
                "Real-time message synchronization",
                "User authentication and authorization",
                "Message encryption",
                "User presence indicators",
                "Media file sharing"
            ),
            githubUrl = "https://github.com/yourusername/chat-app",
            startDate = "2024-07",
            endDate = "2024-09",
            featured = true
        ),
        Project(
            id = "3",
            title = "E-Commerce Platform",
            description = "A comprehensive e-commerce solution with product catalog, shopping cart, and payment integration.",
            technologies = listOf("Kotlin", "Room Database", "Retrofit", "Stripe API"),
            features = listOf(
                "Product search and filtering",
                "Secure payment processing",
                "Order tracking",
                "User reviews and ratings",
                "Wishlist management"
            ),
            startDate = "2024-05",
            endDate = "2024-08"
        ),
        Project(
            id = "4",
            title = "Task Management CLI Tool",
            description = "An efficient command-line tool for managing tasks with local persistence and advanced filtering options.",
            technologies = listOf("Kotlin", "Coroutines", "SQLite", "Arrow"),
            features = listOf(
                "Create, read, update, delete tasks",
                "Priority and due date tracking",
                "Local data persistence",
                "Advanced search and filtering",
                "Recurring task support"
            ),
            startDate = "2024-04",
            endDate = "2024-06"
        ),
        Project(
            id = "5",
            title = "Weather Dashboard",
            description = "A responsive weather app displaying real-time weather data with forecasts and location-based features.",
            technologies = listOf("Kotlin", "OpenWeather API", "Jetpack Compose", "Location Services"),
            features = listOf(
                "Real-time weather updates",
                "5-day forecast",
                "Location auto-detection",
                "Weather alerts",
                "Multiple location support"
            ),
            startDate = "2024-02",
            endDate = "2024-04"
        )
    )

    val experiences = listOf(
        Experience(
            id = "1",
            title = "Senior Kotlin Developer",
            company = "TechCorp Solutions",
            startDate = "2023-06",
            endDate = null,
            description = "Leading mobile and multiplatform development projects with a focus on clean architecture and user experience.",
            skills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines", "Microservices"),
            isCurrent = true
        ),
        Experience(
            id = "2",
            title = "Android Developer",
            company = "Digital Innovations Inc",
            startDate = "2021-03",
            endDate = "2023-05",
            description = "Developed and maintained multiple production Android applications with 100K+ downloads.",
            skills = listOf("Android", "Kotlin", "Java", "Firebase", "REST APIs"),
            isCurrent = false
        ),
        Experience(
            id = "3",
            title = "Junior Software Engineer",
            company = "StartUp Ventures",
            startDate = "2020-01",
            endDate = "2021-02",
            description = "Contributed to full-stack development projects and gained experience with various technologies.",
            skills = listOf("Java", "Python", "JavaScript", "SQL", "Git"),
            isCurrent = false
        )
    )

    val skills = listOf(
        // Languages
        Skill("Kotlin", 5, "Language"),
        Skill("Java", 4, "Language"),
        Skill("Python", 3, "Language"),
        Skill("JavaScript", 3, "Language"),
        
        // Mobile & Multiplatform
        Skill("Jetpack Compose", 5, "Framework"),
        Skill("Android", 5, "Framework"),
        Skill("Compose Multiplatform", 4, "Framework"),
        Skill("Material Design 3", 5, "Framework"),
        
        // Backend & Database
        Skill("Kotlin Coroutines", 5, "Framework"),
        Skill("Firebase", 4, "Backend"),
        Skill("SQLite", 4, "Database"),
        Skill("Room Database", 4, "Database"),
        Skill("REST APIs", 4, "Backend"),
        
        // Tools & DevOps
        Skill("Git", 5, "Tool"),
        Skill("Gradle", 4, "Tool"),
        Skill("CI/CD", 3, "Tool"),
        Skill("Docker", 3, "Tool"),
        
        // Design & UX
        Skill("UI/UX Design", 4, "Design"),
        Skill("Figma", 3, "Design"),
        Skill("Accessibility", 4, "Design")
    )

    val contact = Contact(
        email = "hello@clency.dev",
        linkedIn = "https://linkedin.com/in/clency",
        github = "https://github.com/clency",
        twitter = "https://twitter.com/clency"
    )

    val bio = """
        Full-Stack Developer & Designer with a passion for creating beautiful, functional applications 
        that solve real-world problems. I specialize in Kotlin multiplatform development and have a keen 
        interest in clean architecture, user experience, and open-source contributions.
        
        When I'm not coding, you can find me exploring new technologies, contributing to open-source 
        projects, or writing about mobile development on my blog.
    """.trimIndent()

    val funFact = "I've built apps that have been downloaded over 500K times and serve users across 50+ countries!"
}
