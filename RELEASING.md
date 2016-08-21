* `./gradlew prepareForRelease` (In case stop releasing after running this command, make sure removing `.travis/release`)
* Add CHANGELOG.md to what changes for new version
* Add plugin.xml change note
```xml
    <change-notes><![CDATA[
        <p>1.0.1</p>
        <ul>
            <li>Fix issue #2 invalid revision range</li>
            <li>Fix issue #5 show error/info notifications</li>
        </ul>
    ]]>
    </change-notes>
```
* Commit & push changes
* Create Release Tag (Upload archive file as well)
* Check [travis ci](https://travis-ci.org/shiraji/intellij-plugin-with-gradle-wizard) to successfully release the module