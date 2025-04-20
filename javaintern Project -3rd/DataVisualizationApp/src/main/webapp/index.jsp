<!DOCTYPE html>
<html>
<head>
    <title>Data Visualization App</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
    <div class="main-container">
        <h2>Data Visualization Tool</h2>
        <div class="drop-area" id="drop-area">
            <p>Drag and drop your file here</p>
               <p>Only csv files are accepted here(MS-DOS)<img src="img/excel-a-icon.png" style="width:28px; height:28px; vertical-align:middle; margin-left:8px;"></p>
        </div>

        <form action="VisualizeServlet" method="post" enctype="multipart/form-data">
            <label class="custom-file-upload">
                <input type="file" name="file" id="fileInput" accept=".csv, .xls, .xlsx" required>
                Select File
            </label>
            <button type="submit">Visualize</button>
        </form>
    </div>

    <script src="js/dragdrop.js"></script>
</body>
</html>
