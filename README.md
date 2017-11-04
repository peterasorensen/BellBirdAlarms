Knowing that only 195 minutes was given for the entirety of this project, I prioritized my time in such a way that I
would have a running application which could be queried by a user and where all of the queries would succeed and work
if queried properly. I assumed that an interface for the project was not necessary, given the time constraints. I also
(under time pressure) was not able to fully design an easily usable user experience (right now all of the work is done
the public static void main method, and has to be changed each time to update a new operation), however this could
easily be fixed by passing in arguments to call this file, or even better -- using Flask to create web URLs to query
this information. Given another hour or two, I think I could make this happen, but not in the 195 minutes allotted. I
prioritized my time to help finish the Upvote() method rather than that user experience.

For the developer currently looking over my project, (forgive the sloppiness), but you can run my code by commenting
in/out the lines regarding "ConnectToDB", "CreateTable", "CreateAlarm", and "ViewAlarm". Also keep in mind you'll need the
sqlite-jdbc-3.20.0.jar file (in this project) to be able to run the code properly and that the project is in
Java 8.

One thing I also would've done with more time was create a Users table, so that Users could only upvote once. As of now,
any user can upvote an unlimited number of times.

Another issue is security problems. This application does not take into consideration SQL Injection attackers. This
issue could also easily be fixed by using Prepared Statements in SQL.

And one final thing is that testing wasn't as vigorous as I would have liked it to be.
