//create database
db = db.getSiblingDB('candidate_db');

//create user
db.createUser({
    user: "candidate_user",
    pwd: "candidate_password",
    roles: [
        {
            role: "readWrite",
            db: "candidate_db"
        }
    ]
});

//create collection
db.createCollection('candidate');

//create docs
db.candidate.insertMany(
    [
        {
            _id: "1",
            name: "Artem Shyshkin",
            skills: ["java", "git", "spring", "mongo", "postgres", "docker"]
        },
        {
            _id: "2",
            name: "Kateryna Shyshkin",
            skills: ["jira", "qa", "git"]
        },
        {
            _id: "3",
            name: "Arina Shyshkina",
            skills: ["english", "scratch", "climbing", "graphity"]
        },
        {
            _id: "4",
            name: "Nazar Shyshkin",
            skills: ["painting", "gaming", "singing"]
        }
    ]
)