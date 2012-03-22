PROJECT=geoloc4d
MAVEN=mvn-3.0

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

archive-lib: compile
	mkdir -p $(PROJECT)-lib/target
	tar -C $(PROJECT)-lib -cvzf target/$(PROJECT)-lib-src.tgz \
	src \
	--exclude=.svn
	@-tar -C $(PROJECT)-lib -cvzf target/$(PROJECT)-lib-target.tgz \
	target/classes target/test-classes target/surefire-reports \
	--exclude=.svn '--exclude=$(PROJECT)-*.tgz'
	cp $(PROJECT)-lib/target/$(PROJECT)-lib-*.jar target

archive-dpws: compile
	mkdir -p $(PROJECT)-dpws/target
	tar -C $(PROJECT)-dpws -cvzf target/$(PROJECT)-dpws-src.tgz \
	src \
	--exclude=.svn
	@-tar -C $(PROJECT)-dpws -cvzf target/$(PROJECT)-dpws-target.tgz \
	target/classes target/test-classes target/surefire-reports \
	--exclude=.svn '--exclude=$(PROJECT)-*.tgz'
	cp $(PROJECT)-dpws/target/$(PROJECT)-dpws-*.jar target

archive-rest: compile
	mkdir -p $(PROJECT)-rest/target
	tar -C $(PROJECT)-rest -cvzf target/$(PROJECT)-rest-src.tgz \
	src \
	--exclude=.svn
	@-tar -C $(PROJECT)-rest -cvzf target/$(PROJECT)-rest-target.tgz \
	target/classes target/test-classes target/surefire-reports \
	--exclude=.svn '--exclude=$(PROJECT)-*.tgz'
	cp $(PROJECT)-rest/target/$(PROJECT)-rest-*.jar target

archive: archive-lib archive-dpws archive-rest

fit: archive
	scp \
	target/$(PROJECT)-lib-src.tgz target/$(PROJECT)-lib-target.tgz target/$(PROJECT)-lib-*.jar \
	target/$(PROJECT)-dpws-src.tgz target/$(PROJECT)-dpws-target.tgz target/$(PROJECT)-dpws-*.jar \
	rychly@kazi.fit.vutbr.cz:/home/users/rychly/public_html/geoloc4d/
	scp \
	target/$(PROJECT)-rest-src.tgz target/$(PROJECT)-rest-target.tgz target/$(PROJECT)-rest-*.jar \
	rychly@kazi.fit.vutbr.cz:/home/users/rychly/public_html/geoloc4d-rest/
