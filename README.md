Welcome to Jellystone V2 (COMP2000 group)

Members:
- Chris
- Abbas
- Lolo
- Samiha
- Atharv
- Gabriel
- Matt

Collusion is encouraged!

## Run the A2 simulation

Use JDK 17 or newer. In VS Code, open the Jellystone folder containing both
`src` and `images`. Open `src/Main.java` and click **Run** above the main
method. In the terminal, enter a number from the ignition menu and press Enter.
All six choices start in the mixed-species forest.

| Number | Ignition |
| --- | --- |
| 1 | Lightning |
| 2 | Arson |
| 3 | Backburning |
| 4 | Zeus' Heavenly Judgment |
| 5 | Fire Serpent |
| 6 | Phoenix Rising |

You can also compile and open the same menu from PowerShell in the Jellystone folder:

```powershell
$buildDir = Join-Path $env:TEMP ("jellystone-" + [guid]::NewGuid())
New-Item -ItemType Directory -Path $buildDir | Out-Null
javac -d $buildDir (Get-ChildItem src -Filter '*.java').FullName
if ($LASTEXITCODE -eq 0) { java -cp $buildDir Main }
```

Close the window before running another command in the same terminal:

| Command | What to look for |
| --- | --- |
| `java -cp $buildDir Main zeus` | A blue lightning bolt. |
| `java -cp $buildDir Main phoenix` | A purple ring with an initially untouched centre. |
| `java -cp $buildDir Main serpent` | A magenta snake with a tapered tail and a wider head. |
| `java -cp $buildDir Main replay 42` | Terminal comparison of survivors with and without Guardians. |
| `java -cp $buildDir Main benchmark` | Terminal comparison of sequential and parallel spread. |

In the visual modes, look for four golden Guardians and their green protection
zones. When a Guardian dies, a brief gold pulse extinguishes nearby fires.
After the last fire goes out, Ash Bloom adds seedlings to burnt cells next to
surviving trees. The terminal prints starting and surviving counts by species.
