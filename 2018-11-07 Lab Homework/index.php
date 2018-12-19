<?php
/**
 * Name: John Meyer
 * Date: 11/7/18
 * Assignment: 11/7 Lab
 */

session_start();

$numVisits = $_SESSION['$numVisits'] ?? 0;
$numVisits++;
$_SESSION['$numVisits'] = $numVisits;

// htmlspecialchars is unnecessary since $cmd is not going to be sent back to the user as HTML
$cmd = htmlspecialchars($_GET['cmd'] ?? '');

$_SESSION['user'] = $_POST['user'] ?? $_SESSION['user'] ?? '';
$user = $_SESSION['user'];
?>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css"/>

    <title>Hello, world!</title>
</head>
<body>

<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">11/7 Lab</a>
</nav>
<div id="body" class="container">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
            <div class="sidebar-sticky">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="index.php">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="index.php?cmd=page1">Random Numbers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="index.php?cmd=page2">Images</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="index.php?cmd=page3">Input Form</a>
                    </li>
                </ul>
                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                    <span>Session Information</span>
                </h6>
                <ul class="nav flex-column mb-2">
                    <li class="nav-item">
                        <span class="nav-link">
                        Visits:
                            <?php
                            echo $numVisits;
                            ?>
                        </span>
                    </li>
                    <?php
                    if ($user !== '') {
                        $user_sanitized = htmlspecialchars($user);
                        echo "<li class=\"nav-item\"><span class=\"nav-link\">User name: $user_sanitized</span></li>";
                    }
                    ?>
                </ul>
            </div>
        </nav>
        <main class="col-md-9 ml-sm-auto col-lg-10 px-4" role="main">
            <?php
            if ($cmd === 'page1') {
                ?>
                <table class="table">
                    <tbody>
                    <?php
                    foreach (range(0, 10) as $i) {
                        echo "<tr>";
                        foreach (range(0, 10) as $j) {
                            $randomValue = rand(0, 100);
                            $red = rand(0, 256);
                            $green = rand(0, 256);
                            $blue = rand(0, 256);

                            echo "<td style='color: rgb($red, $green, $blue);'>$randomValue</td>";
                        }
                        echo "</tr>";
                    }
                    ?>
                    </tbody>
                </table>
                <?php
            } else if ($cmd === 'page2') {
                $images = [
                    'assets/firefox-logo.svg',
                    'assets/react-icon.svg',
                    'assets/vue.js-logo.svg',
                ];
                $selectedImage = $images[array_rand($images)];
                echo "<img src='$selectedImage' />";
            } else if ($cmd === 'page3') {
                ?>
                <form action="index.php" method="post">
                    <div class="form-group">
                        <label for="name">Email address</label>
                        <input type="text" class="form-control" id="name" name="user" aria-describedby="name-help"
                               placeholder="Enter name">
                        <small id="name-help" class="form-text text-muted">We'll greet you by name the next time you
                            visit.
                        </small>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
                <?php
            } else {
                ?>
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">Variable</th>
                        <th scope="col">Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">HTTPS</th>
                        <td>
                            <?php
                            echo $_SERVER['HTTPS'] ?? '';
                            ?>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">SSL Protocol</th>
                        <td>
                            <?php
                            echo $_SERVER['SSL_PROTOCOL'] ?? '';
                            ?>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">TLS SNI</th>
                        <td>
                            <?php
                            echo $_SERVER['SSL_TLS_SNI'] ?? '';
                            ?>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">DNT</th>
                        <td>
                            <?php
                            echo $_SERVER['HTTP_DNT'] ?? '';
                            ?>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">Remote address</th>
                        <td>
                            <?php
                            echo $_SERVER['REMOTE_ADDR'] ?? '';
                            ?>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <?php
            }
            ?>
        </main>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>
</body>
</html>