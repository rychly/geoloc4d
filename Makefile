PROJECT=geoloc4d
MAVEN=mvn-2.2

test:
	$(MAVEN) test

compile:
	$(MAVEN) compile

deploy:
	$(MAVEN) deploy

site:
	$(MAVEN) site:site
	$(MAVEN) site:deploy

clean:
	$(MAVEN) clean

archive: deploy
	mkdir -p target
	tar cvzf target/$(PROJECT)-src.tgz \
	src \
	--exclude=.svn
	tar cvzf target/$(PROJECT)-target.tgz \
	target/classes target/*.jar target/test-classes target/surefire-reports \
	--exclude=.svn '--exclude=$(PROJECT)-*.tgz'

fit: archive
	scp target/$(PROJECT)-src.tgz target/$(PROJECT)-target.tgz rychly@kazi.fit.vutbr.cz:/home/users/rychly/public_html/geoloc4d/
