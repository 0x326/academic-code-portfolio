<?php
/*
 * Name: John Meyer
 * Date: 11/16/18
 * Assignment: 11/14 Homework
 */

session_start();

require_once('database-credentials.php');
$database = new mysqli($database_host, $database_username, $database_password, 'cse383');

$username = $_SESSION['username'] ?? '';

parse_str($_SERVER['QUERY_STRING'], $urlQueries);
$action = $urlQueries['action'] ?? '';
if (isset($urlQueries['minSalary'])) {
    $minSalary = (int)$urlQueries['minSalary'];
    if ($minSalary === 0) {
        unset($minSalary);
    }
}

if ($action === 'login' && isset($_POST['username']) && isset($_POST['password'])) {
    $query = $database->prepare('select user as username, password as passwordHash
                                       from users
                                       where user = ?;');
    $query->bind_param('s', $_POST['username']);
    $query->execute();
    $query = $query->get_result();
    // Note: Though there should only be one selected tuple, the database does not make us any guarantees that
    // there will not be more since 'user' is not a primary key
    $validCredentials = false;
    foreach ($query->fetch_all(MYSQLI_ASSOC) as $rowNumber => $row) {
        if (password_verify($_POST['password'], $row['passwordHash']) === true) {
            $_SESSION['username'] = $row['username'];
            header("Location: {$_SERVER['PHP_SELF']}");
            exit();
        }
    }
} else if ($action !== 'login' && $username === '') {
    header("Location: {$_SERVER['PHP_SELF']}?action=login");
    exit();
} else if ($action === 'logout') {
    unset($_SESSION['username']);
    header("Location: {$_SERVER['PHP_SELF']}?action=login");
    exit();
}

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
    <style>
        body {
            padding-top: 40px;
            padding-bottom: 40px;
        }
    </style>

    <title>11/14 Homework</title>
</head>
<body>

<div class="container">

    <?php
    if ($username === '') {
        ?>

        <?php
        if (isset($validCredentials) && $validCredentials === false) {
            echo '<div class="alert alert-danger" role="alert">
              Invalid username or password
            </div>';
        }
        ?>

        <form method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" class="form-control" id="username" name="username" aria-describedby="emailHelp"
                       placeholder="Enter email">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Password">
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>

        <?php
    } elseif (!$database->connect_errno) {
        $queryString = '';
        if (isset($minSalary)) {
            $query = $database->prepare('select SchoolName as schoolName,
                                                 NumUndergrads as undergraduates,
                                                 Median10YearEarnings as median10YearSalary,
                                                 url as url
                                               from SchoolData
                                               where Median10YearEarnings >= ?;');
            // TODO: Check minSalary
            $query->bind_param('i', $minSalary);
            $query->execute();
            $query = $query->get_result();
        } else {
            $query = $database->query('select SchoolName as schoolName,
                                               NumUndergrads as undergraduates,
                                               Median10YearEarnings as median10YearSalary,
                                               url as url
                                             from SchoolData;');
        }
        if ($query) {
            ?>
            <div class="row">
                <div class="col-2">
                    <form method="post" action="lab11-14.php?action=logout">
                        <button type="submit" class="btn btn-primary">Logout</button>
                    </form>
                </div>

                <div class="col-10">
                    <form method="get">
                        <div class="form-group row">
                            <label for="filter" class="col-4 col-form-label">Filter by median salary</label>
                            <div class="col">
                                <input name="minSalary" type="number" class="form-control" id="filter"
                                       placeholder="Minimum median salary"
                                    <?php
                                    if (isset($minSalary)) {
                                        echo "value=\"$minSalary\"";
                                    }
                                    ?>
                                >
                            </div>
                            <div class="col-2">
                                <button type="submit" class="btn btn-primary">Filter</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div>
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">School Name</th>
                        <th scope="col"># of Undergraduates</th>
                        <th scope="col">Median Salary</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php
                    foreach ($query->fetch_all(MYSQLI_ASSOC) as $rowNumber => $row) {
                        $school = htmlspecialchars($row['schoolName']);
                        $undergraduates = htmlspecialchars($row['undergraduates']);
                        $medianSalary = htmlspecialchars($row['median10YearSalary']);

                        $urlString = '';
                        $url = parse_url($row['url']);

                        if ($url) {
                            // Scheme
                            if (!isset($url['scheme'])) {
                                // Assume HTTP
                                $url = parse_url('http://' . $row['url']);
                            }
                            // parse_url sufficiently checks the scheme

                            // Host
                            $urlHostString = idn_to_ascii($url['host']);
                            $urlHostString = filter_var($urlHostString, FILTER_VALIDATE_DOMAIN);

                            // Port
                            $urlPortString = '';
                            if (isset($url['port'])) {
                                // $url['port'] is an int so this is safe
                                $urlPortString = ':' . $url['port'];
                            }

                            // Username & password
                            $urlCredentialString = '';
                            if (isset($url['user'])) {
                                $urlCredentialString = urlencode($url['user']);
                                if (isset($url['pass'])) {
                                    $urlCredentialString .= ':' . urlencode($url['pass']);
                                }
                                $urlCredentialString .= '@';
                            }

                            // Path
                            $urlPathString = '';
                            if (isset($url['path'])) {
                                $urlPathParts = explode('/', $url['path']);
                                $urlPathParts = array_map(function ($path) {
                                    return urlencode($path);
                                }, $urlPathParts);
                                $urlPathString = implode('/', $urlPathParts);
                            }

                            // Query
                            $urlQueryString = '';
                            if (isset($url['query'])) {
                                $urlQueryParts = explode('&', $url['query']);
                                $urlQueryParts = array_map(function ($queryPart) {
                                    $querySubParts = explode('=', $queryPart);
                                    $querySubParts = array_map(function ($subPart) {
                                        return urlencode($subPart);
                                    }, $querySubParts);
                                    return implode('=', $querySubParts);
                                }, $urlQueryParts);
                                $urlQueryString = '?' . implode('&', $urlQueryParts);
                            }

                            // Fragment
                            $urlFragmentString = '';
                            if (isset($url['fragment'])) {
                                $urlFragmentString = '#' . urlencode($url['fragment']);
                            }

                            if ($urlHostString) {
                                $urlString = "{$url['scheme']}://$urlCredentialString$urlHostString$urlPortString$urlPathString$urlQueryString$urlFragmentString";
                            }
                        }

                        echo '<tr>';

                        echo '<td>';
                        if ($urlString) {
                            echo "<a href='$urlString'>$school</a>";
                        } else {
                            echo $school;
                        }
                        echo '</td>';

                        echo "<td>$undergraduates</td>";
                        echo "<td>$medianSalary</td>";
                        echo '</tr>';
                    }
                    ?>
                    </tbody>
                </table>
            </div>
            <?php
        }
    } else {
        ?>

        <p>Data not available</p>

        <?php
    }
    ?>

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
