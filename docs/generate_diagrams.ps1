Add-Type -AssemblyName System.Drawing

$OutDir = Join-Path $PSScriptRoot "."

function New-Canvas($path, $width, $height) {
    $bitmap = New-Object System.Drawing.Bitmap($width, $height)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $graphics.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::ClearTypeGridFit
    $graphics.Clear([System.Drawing.Color]::FromArgb(244, 239, 228))
    return @{ Bitmap = $bitmap; Graphics = $graphics; Path = $path }
}

function Save-Canvas($canvas) {
    $canvas.Bitmap.Save($canvas.Path, [System.Drawing.Imaging.ImageFormat]::Png)
    $canvas.Graphics.Dispose()
    $canvas.Bitmap.Dispose()
}

function Font($size, $style = [System.Drawing.FontStyle]::Regular) {
    return New-Object System.Drawing.Font("Arial", $size, $style)
}

function Brush($hex) {
    return New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml($hex))
}

function Pen($hex, $width = 2) {
    return New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml($hex), $width)
}

function Draw-Title($g, $text) {
    $font = Font 24 ([System.Drawing.FontStyle]::Bold)
    $g.DrawString($text, $font, (Brush "#211d18"), 42, 28)
    $font.Dispose()
}

function Draw-Box($g, $x, $y, $w, $h, $title, $fill, $body = "") {
    $shadow = Brush "#c8bda9"
    $g.FillRectangle($shadow, $x + 5, $y + 5, $w, $h)
    $shadow.Dispose()

    $brush = Brush $fill
    $border = Pen "#4a392b" 2
    $g.FillRectangle($brush, $x, $y, $w, $h)
    $g.DrawRectangle($border, $x, $y, $w, $h)

    $titleFont = Font 11 ([System.Drawing.FontStyle]::Bold)
    $bodyFont = Font 8
    $g.DrawString($title, $titleFont, (Brush "#231f1a"), $x + 10, $y + 9)
    if ($body -ne "") {
        $g.DrawLine((Pen "#6b5746" 1), $x, $y + 32, $x + $w, $y + 32)
        $g.DrawString($body, $bodyFont, (Brush "#2b2723"), $x + 10, $y + 39)
    }

    $brush.Dispose()
    $border.Dispose()
    $titleFont.Dispose()
    $bodyFont.Dispose()
}

function Draw-Line($g, $x1, $y1, $x2, $y2, $label = "") {
    $pen = Pen "#6d5134" 2
    $g.DrawLine($pen, $x1, $y1, $x2, $y2)

    $angle = [Math]::Atan2($y2 - $y1, $x2 - $x1)
    $size = 10
    $p1 = New-Object System.Drawing.PointF(
        [float]($x2 - $size * [Math]::Cos($angle - 0.45)),
        [float]($y2 - $size * [Math]::Sin($angle - 0.45))
    )
    $p2 = New-Object System.Drawing.PointF(
        [float]($x2 - $size * [Math]::Cos($angle + 0.45)),
        [float]($y2 - $size * [Math]::Sin($angle + 0.45))
    )
    $g.FillPolygon((Brush "#6d5134"), @(
        (New-Object System.Drawing.PointF([float]$x2, [float]$y2)),
        $p1,
        $p2
    ))

    if ($label -ne "") {
        $font = Font 8
        $lx = ($x1 + $x2) / 2 + 5
        $ly = ($y1 + $y2) / 2 - 14
        $g.FillRectangle((Brush "#f4efe4"), $lx - 3, $ly - 1, 110, 16)
        $g.DrawString($label, $font, (Brush "#4b3928"), $lx, $ly)
        $font.Dispose()
    }
    $pen.Dispose()
}

function Draw-ClassDiagram {
    $canvas = New-Canvas (Join-Path $OutDir "class-diagram.png") 1450 980
    $g = $canvas.Graphics
    Draw-Title $g "Maze Runner - UML Class Diagram"

    Draw-Box $g 60 105 170 70 "Main" "#dec99e" "extends Game"
    Draw-Box $g 300 95 220 90 "GameScreen" "#c8d9bb" "implements Screen`nimplements HPObserver"
    Draw-Box $g 590 95 170 70 "GameFacade" "#bfd0e6" "screen transitions"
    Draw-Box $g 830 95 190 70 "AudioManager" "#d6c0e6" "Singleton"

    Draw-Box $g 70 255 205 95 "Player" "#ead3a9" "hp, speed, inventory`nactiveItem, facing"
    Draw-Box $g 335 255 190 80 "PlayerInputHandler" "#d8c8e8" "creates commands"
    Draw-Box $g 600 245 170 95 "ICommand" "#f0dfb5" "execute()"
    Draw-Box $g 830 230 165 80 "MoveCommand" "#f4cabd"
    Draw-Box $g 1030 230 165 80 "InteractCommand" "#f4cabd"
    Draw-Box $g 1230 230 165 80 "StealthKillCommand" "#f4cabd"

    Draw-Box $g 70 455 210 95 "LevelMap" "#bfe1dc" "items, doors, enemies`ncollision, LoS"
    Draw-Box $g 340 455 180 70 "ILevelMap" "#d7e7c5" "getTile()"
    Draw-Box $g 585 455 220 70 "ArrayLevelMapAdapter" "#d7e7c5" "implements ILevelMap"

    Draw-Box $g 70 640 190 90 "Enemy" "#e7b0aa" "state + strategy`nFOV, chase, search"
    Draw-Box $g 320 640 170 70 "EnemyState" "#f0c9a4" "PATROL/CHASE/... "
    Draw-Box $g 560 620 195 90 "MovementStrategy" "#dbc8ec" "move(enemy, world)"
    Draw-Box $g 825 590 150 62 "PatrolStrategy" "#ead3a9"
    Draw-Box $g 1005 590 150 62 "ChaseStrategy" "#ead3a9"
    Draw-Box $g 825 690 150 62 "SearchStrategy" "#ead3a9"
    Draw-Box $g 1005 690 150 62 "ReturnStrategy" "#ead3a9"

    Draw-Box $g 70 820 160 70 "Item" "#dfe8bd" "interface"
    Draw-Box $g 290 805 145 62 "Key" "#efd28e"
    Draw-Box $g 460 805 145 62 "Crowbar" "#efd28e"
    Draw-Box $g 630 805 145 62 "Knife" "#efd28e"
    Draw-Box $g 800 805 145 62 "Medkit" "#efd28e"
    Draw-Box $g 970 805 145 62 "Disguise" "#efd28e"
    Draw-Box $g 1180 805 170 70 "ItemFactory" "#d6c0e6" "create(type)"

    Draw-Box $g 1060 430 145 70 "Door" "#dfe8bd" "abstract"
    Draw-Box $g 1245 390 145 62 "BaseDoor" "#c8d9bb"
    Draw-Box $g 1245 485 145 62 "LockedDoor" "#c8d9bb" "Decorator"
    Draw-Box $g 1060 555 165 70 "DoorEntity" "#bfd0e6" "render + bounds"

    Draw-Line $g 230 140 300 140
    Draw-Line $g 520 140 590 130
    Draw-Line $g 760 130 830 130
    Draw-Line $g 300 280 275 290
    Draw-Line $g 525 295 600 292
    Draw-Line $g 770 290 830 270
    Draw-Line $g 770 292 1030 270
    Draw-Line $g 770 295 1230 270
    Draw-Line $g 360 185 170 255
    Draw-Line $g 405 185 170 455
    Draw-Line $g 280 500 340 492
    Draw-Line $g 520 492 585 492
    Draw-Line $g 170 550 165 640
    Draw-Line $g 260 685 320 675
    Draw-Line $g 490 675 560 665
    Draw-Line $g 755 650 825 620
    Draw-Line $g 755 660 1005 620
    Draw-Line $g 755 675 825 720
    Draw-Line $g 755 685 1005 720
    Draw-Line $g 280 500 1060 590
    Draw-Line $g 1205 465 1245 420
    Draw-Line $g 1205 465 1245 515
    Draw-Line $g 1350 835 1115 835
    Draw-Line $g 230 855 290 835
    Draw-Line $g 230 855 460 835
    Draw-Line $g 230 855 630 835
    Draw-Line $g 230 855 800 835
    Draw-Line $g 230 855 970 835

    Save-Canvas $canvas
}

function Draw-GameFlowDiagram {
    $canvas = New-Canvas (Join-Path $OutDir "game-flow-diagram.png") 1250 820
    $g = $canvas.Graphics
    Draw-Title $g "Maze Runner - Game Flow Diagram"

    Draw-Box $g 520 105 220 92 "Main Menu" "#dec99e" "Play`nQuit`nM - music"
    Draw-Box $g 510 285 240 115 "Game Screen" "#c8d9bb" "Levels 1-4`nmovement + stealth`nitems + doors"
    Draw-Box $g 880 285 225 105 "Pause Overlay" "#bfd0e6" "Resume`nRestart`nMain Menu"
    Draw-Box $g 510 535 240 105 "Level Complete" "#ead3a9" "Continue`nMain Menu"
    Draw-Box $g 850 535 230 105 "Win Screen" "#b8ddc8" "Final portal`nMain Menu"
    Draw-Box $g 165 535 230 105 "Game Over" "#e7b0aa" "HP = 0`nRetry / Menu"

    Draw-Line $g 630 197 630 285
    Draw-Line $g 750 340 880 338
    Draw-Line $g 880 365 750 365
    Draw-Line $g 1000 390 740 285
    Draw-Line $g 990 285 740 155
    Draw-Line $g 630 400 630 535
    Draw-Line $g 630 640 630 715
    Draw-Line $g 750 588 850 588
    Draw-Line $g 510 588 395 588
    Draw-Line $g 280 535 520 190
    Draw-Line $g 280 640 520 160
    Draw-Line $g 965 535 740 155

    $font = Font 12
    $g.DrawString("Transitions: Play -> Game, Esc -> Pause, Resume -> Game, Restart -> Game, HP 0 -> Game Over,", $font, (Brush "#4b3928"), 265, 705)
    $g.DrawString("Portal -> Level Complete, Continue -> Next Level, Level 4 Portal -> Win, Menu buttons -> Main Menu.", $font, (Brush "#4b3928"), 265, 735)
    $g.DrawString("Progression: Level 1 -> Level 2 -> Level 3 -> Level 4 -> Win", $font, (Brush "#4b3928"), 265, 765)
    $font.Dispose()

    Save-Canvas $canvas
}

function Draw-LevelSketch {
    $canvas = New-Canvas (Join-Path $OutDir "level-1-sketch.png") 1300 930
    $g = $canvas.Graphics
    Draw-Title $g "Maze Runner - Level 1 Sketch"

    $map = @(
        @(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
        @(1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1),
        @(1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1),
        @(1,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,1),
        @(1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,0,1,1),
        @(1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1),
        @(1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1),
        @(1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,1,1),
        @(1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,0,1,1),
        @(1,0,1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,1),
        @(1,0,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1),
        @(1,0,0,0,1,0,0,0,0,0,1,0,0,0,1,0,1,1),
        @(1,1,1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,1),
        @(1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1),
        @(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
    )

    $tile = 42
    $ox = 120
    $oy = 135
    $rows = $map.Count
    $cols = $map[0].Count

    for ($r = 0; $r -lt $rows; $r++) {
        for ($c = 0; $c -lt $cols; $c++) {
            $x = $ox + $c * $tile
            $y = $oy + ($rows - 1 - $r) * $tile
            if ($map[$r][$c] -eq 1) {
                $g.FillRectangle((Brush "#3b4544"), $x, $y, $tile, $tile)
                $g.DrawRectangle((Pen "#1a2423" 1), $x, $y, $tile, $tile)
                $g.FillRectangle((Brush "#53605e"), $x + 5, $y + 5, $tile - 10, 3)
            } else {
                $g.FillRectangle((Brush "#151d1d"), $x, $y, $tile, $tile)
                $g.DrawRectangle((Pen "#26302f" 1), $x, $y, $tile, $tile)
            }
        }
    }

    function TileRect($g, $c, $r, $color, $label) {
        $tile = 42
        $ox = 120
        $oy = 135
        $rows = 15
        $x = $ox + $c * $tile + 4
        $y = $oy + ($rows - 1 - $r) * $tile + 4
        $g.FillRectangle((Brush $color), $x, $y, $tile - 8, $tile - 8)
        $font = Font 8 ([System.Drawing.FontStyle]::Bold)
        $g.DrawString($label, $font, (Brush "#101010"), $x + 3, $y + 10)
        $font.Dispose()
    }

    TileRect $g 1 1 "#9fd4be" "Spawn"
    TileRect $g 15 1 "#f2c34b" "Key"
    TileRect $g 15 13 "#b47a36" "Door"
    TileRect $g 16 13 "#57e4d1" "Exit"

    $legendX = 930
    Draw-Box $g $legendX 180 260 280 "Legend" "#ead3a9" "Dark floor - walkable path`nGray blocks - walls`nSpawn - player start`nKey - required item`nDoor - wooden locked door`nExit - portal to Level 2"

    $font = Font 12
    $g.DrawString("Level goal: reach the key first, return to the top corridor, open the wooden door, then step into the portal.", $font, (Brush "#4b3928"), 120, 800)
    $font.Dispose()

    Save-Canvas $canvas
}

Draw-ClassDiagram
Draw-GameFlowDiagram
Draw-LevelSketch
