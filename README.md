PredictWeasel
=============
PredictWeasel, now into its third major version, is a game where players predict the final score of games played between two teams or individuals. It is ideally suited to games of soccer but would work just as well for all flavours of hockey (i.e. games where goals score one point each). With a small tweak it could be made to work for rugby or similar, higher scoring games.

In its first incarnation it had predictions being submitted by email, and a static website generated from the results every day. Version two saw the transition to a web application written in Java using Apache Struts for its MVC framework. The front end was initially hand crafted by me and so, as I have zero design talent, and not much more ability, in this department, it was basic to say the least.

It was always intended to rewrite it using something more modern but there were a few false starts, including one where I tried to use Grails, before I finally managed it in 2014. The first major change was the switch to using Spring and, in particular, Spring Boot. It's incredible and now a must for any new Java project I start. Change number two was the UI (I bought a theme based on Bootstrap) which takes so much of the pain out of writing a web application that also works well on mobile devices. It also looks terrific and offers more features than I could ever hope to provide.

The next version will add the league blog functionality that was, surprisingly, greatly missed in 2014's league based around the FIFA World Cup. Then there's a whole load of administrative functionality to add for creating competitions, leagues, fixtures and the rest.

After that I can start adding a few more fancy features, not that I know what they are yet!
