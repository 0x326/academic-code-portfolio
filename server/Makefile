serve:
	php -S localhost:8080 -t src/ -c ./

install:
	composer install

lint:
	php -l src/*.php

test-php:
	./vendor/bin/phpunit --bootstrap vendor/autoload.php test

apt-install:
	apt-get install php composer php-xml php-mbstring php-mysql php-xdebug
