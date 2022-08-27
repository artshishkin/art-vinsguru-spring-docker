//create database
db = db.getSiblingDB('job_db');

//create user
db.createUser({
    user: "job_user",
    pwd: "job_password",
    roles: [
        {
            role: "readWrite",
            db: "job_db"
        }
    ]
});

//create collection
db.createCollection('job');

//create docs
db.job.insertMany(
    [
        {
            "description": "Senior Java Developer",
            "company": "Amazon",
            "skills": ["java", "spring", "mongo", "postgres", "docker"],
            "salary": 100000,
            "remote": true
        },
        {
            "description": "Junior Java Developer",
            "company": "apple",
            "skills": ["java"],
            "salary": 50000,
            "remote": false
        },
        {
            "description": "Scrum Master",
            "company": "google",
            "skills": ["agile", "jira"],
            "salary": 60000,
            "remote": true
        },
        {
            "description": "Senior Angular Developer",
            "company": "Twitter",
            "skills": ["typescript", "nodejs", "angular", "javascript"],
            "salary": 80000,
            "remote": true
        },
        {
            "description": "Director of Engineering",
            "company": "microsoft",
            "skills": ["java", "jira", "project"],
            "salary": 150000,
            "remote": true
        }
    ]
)