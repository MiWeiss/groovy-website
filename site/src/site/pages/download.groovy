import generator.SiteGenerator

layout 'layouts/main.groovy', true,
        pageTitle: 'The Apache Groovy programming language - Download',
        mainContent: contents {
            div(id: 'content', class: 'page-1') {
                div(class: 'row') {
                    div(class: 'row-fluid') {
                        div(class: 'col-lg-3') {
                            ul(class: 'nav-sidebar') {
                                li(class: 'active') {
                                    a(href: 'download.html') { strong('Download Groovy') }
                                }
                                li {
                                    a(href: '#distro', class: 'anchor-link', 'Distributions')
                                }
                                li {
                                    a(href: '#sdkman', class: 'anchor-link', 'Through SDKMAN!')
                                }
                                li {
                                    a(href: '#buildtools', class: 'anchor-link', 'From your build tools')
                                }
                                li {
                                    a(href: '#otherways', class: 'anchor-link', 'Other ways to get Groovy')
                                }
                                li {
                                    a(href: '#requirements', class: 'anchor-link', 'System requirements')
                                }
                                li {
                                    a(href: 'versioning.html', 'Groovy version scheme')
                                }
                                li {
                                    a(href: 'indy.html', 'Invoke dynamic support')
                                }
                                li {
                                    a(href: 'releases.html', 'Release notes')
                                }
                            }
                        }

                        div(class: 'col-lg-8 col-lg-pull-0') {
                            include template: 'includes/contribute-button.groovy'
                            h1 {
                                i(class: 'fa fa-cloud-download') {}
                                yield ' Download'
                            }
                            def linkVersionToDownload = distributions.collect { it.packages }.flatten().find { it.stable }.version
                            button(id: 'big-download-button', type: 'button', class: 'btn btn-default',
                                    title: "Download Groovy ${linkVersionToDownload}\nSee below for verification information",
                                    onclick: "window.location.href=\"https://dl.bintray.com/groovy/maven/apache-groovy-sdk-${linkVersionToDownload}.zip\"") {
                                i(class: 'fa fa-download') {}
                                yield ' Download'
                            }
                            article {
                                p {
                                    yield 'In this download area, you will find the latest Groovy '
                                    a(href: '#distro', 'distributions')
                                    yield ' including binary, source, downloadable documentation and a Windows installer convenience file (if available).'
                                }
                                p {
                                    yield 'Downloads are hosted in:'
                                    ul {
                                        li {
                                            yield "Bintray's "
                                            a(href: 'http://bintray.com/groovy/', 'Groovy repository')
                                            yield '. Register on Bintray to rate, review, and register for new version notifications. Or '
                                            a(href: 'https://dl.bintray.com/groovy/maven/', 'browse')
                                            yield ' all versions.'
                                        }
                                        li {
                                            yield "Apache's "
                                            a(href: 'http://www.apache.org/dyn/closer.cgi/groovy/', 'release mirrors')
                                            yield ' and '
                                            a(href: 'https://archive.apache.org/dist/groovy/', 'archive repository')
                                            yield '.'
                                        }
                                    }
                                }
                                p {
                                    yield 'For a quick and effortless start on Mac OSX, Linux or Cygwin, you can use '
                                    a(href: 'http://sdkman.io/', 'SDKMAN! (The Software Development Kit Manager)')
                                    yield ' to download and configure any Groovy version of your choice. Basic '
                                    a(href: '#sdkman', 'instructions')
                                    yield ' can be found below. '
                                    br()
                                    yield 'Windows users can use '
                                    a(href: 'https://github.com/flofreud/posh-gvm', 'Posh-GVM')
                                    yield ' (POwerSHell Groovy enVironment Manager), a PowerShell clone of the GVM CLI.'
                                }
                            }
                            hr(class: 'divider')

                            a(name: 'distro') {}
                            article {
                                h1 'Distributions'
                                p 'You can download a binary, a source, a documentation bundle or an SDK bundle combining all three. A convenience Windows installer is also provided when available.'
                                p {
                                    yield "We provide OpenPGP signatures ('.asc') files and checksums for every release file. We recommend that you "
                                    a(href: 'https://www.apache.org/info/verification.html', 'verify')
                                    yield ' the integrity of downloaded files by generating your own checksums and matching them against ours and checking signatures using the '
                                    a(href: 'https://www.apache.org/dist/groovy/KEYS', 'KEYS')
                                    yield " file which contains the OpenPGP keys of Groovy's Release Managers."
                                }

                                distributions.each { dist ->
                                    h2 {
                                        i(class: 'fa fa-star') {}
                                        yield " ${dist.name}"
                                    }
                                    if (dist.description) {
                                        p {
                                            dist.description.rehydrate(this, this, this)()
                                        }
                                    }
                                    def archiveUrl = { String type, String area, v -> "https://archive.apache.org/dist/groovy/${v}/${area}/apache-groovy-${type}-${v}.zip".toString() }
                                    def archiveExtUrl = { String type, String area, v, String ext -> "${archiveUrl(type, area, v)}.$ext".toString() }
                                    def distUrl = { String type, String area, v -> "https://www.apache.org/dist/groovy/${v}/${area}/apache-groovy-${type}-${v}.zip".toString() }
                                    def distExtUrl = { String type, String area, v, String ext -> "${distUrl(type, area, v)}.$ext".toString() }
                                    def findUrl = { String type, String area, v, String ext ->
                                        def u = archiveExtUrl(type, area, v, ext)
                                        if (SiteGenerator.exists(u)) return u
                                        u = distExtUrl(type, area, v, ext)
                                        if (SiteGenerator.exists(u)) return u
                                        null
                                    }
                                    def buildExtras = { String type, String area, String v ->
                                        def extras = [:]
                                        def url = findUrl(type, area, v, 'asc')
                                        if (url) { extras.asc = url }
                                        url = findUrl(type, area, v, 'md5')
                                        if (url) { extras.md5 = url }
                                        url = findUrl(type, area, v, 'sha256')
                                        if (url) { extras.sha256 = url }
                                        if (extras) {
                                            def first = true
                                            br()
                                            yield '('
                                            extras.each { ext, u ->
                                                if (first) first = false
                                                else yield ' '
                                                a(href: u, ext)
                                            }
                                            yield ')'
                                        }
                                    }
                                    def srcUrl = { v ->
                                        def u = "http://www.apache.org/dyn/closer.cgi/groovy/${v}/sources/apache-groovy-src-${v}.zip"
                                        if (!SiteGenerator.exists(u)) {
                                            u = archiveUrl('src', 'sources', v)
                                        }
                                        u
                                    }
                                    dist.packages.each { pkg ->
                                        h3 "${pkg.version} distributions"
                                        table(width: '100%', class: 'download-table') {
                                            tr {
                                                td {
                                                    a(href: "https://dl.bintray.com/groovy/maven/apache-groovy-binary-${pkg.version}.zip") {
                                                        i(class: 'fa fa-gears fa-4x') {}
                                                        br()
                                                        yield 'binary'
                                                    }
                                                    buildExtras('binary', 'distribution', pkg.version)
                                                }
                                                td {
                                                    a(href: srcUrl(pkg.version)) {
                                                        i(class: 'fa fa-code fa-4x') {}
                                                        br()
                                                        yield ' source'
                                                    }
                                                    buildExtras('src', 'sources', pkg.version)
                                                }
                                                td {
                                                    a(href: "https://dl.bintray.com/groovy/maven/apache-groovy-docs-${pkg.version}.zip") {
                                                        i(class: 'fa fa-file-text fa-4x') {}
                                                        br()
                                                        yield ' documentation'
                                                    }
                                                    buildExtras('docs', 'distribution', pkg.version)
                                                }
                                                td {
                                                    a(href: "https://dl.bintray.com/groovy/maven/apache-groovy-sdk-${pkg.version}.zip") {
                                                        i(class: 'fa fa-file-zip-o fa-4x') {}
                                                        br()
                                                        yield ' SDK bundle'
                                                    }
                                                    buildExtras('sdk', 'distribution', pkg.version)
                                                }
                                                if (pkg.windowsInstaller) {
                                                    td {
                                                        a(href: pkg.windowsInstaller) {
                                                            i(class: 'fa fa-windows fa-4x') {}
                                                            br()
                                                            yield ' Windows installer'
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        p {
                                            yield 'Please consult the '
                                            a(href: "changelogs/changelog-${pkg.version}.html", ' change log')
                                            yield ' for details. Please read the '
                                            a(href: "indy.html", 'invoke dynamic support information')
                                            yield ' if you like to enable that feature and are using Groovy on JDK 7+.'
                                        }
                                    }
                                }
                                article {
                                    h3 'Changelog'

                                    p {
                                        yield 'You can also read the changelogs for '
                                        a(href: "changelogs.html", 'other versions')
                                        yield '.'
                                    }
                                }
                            }

                            hr(class: 'divider')

                            a(name: 'sdkman') {}
                            article {
                                h1 'SDKMAN! (The Software Development Kit Manager)'
                                p {
                                    yield 'This tool makes installing Groovy on any Bash platform (Mac OSX, Linux, Cygwin, Solaris or FreeBSD) very easy.'
                                    br()
                                    yield 'Simply open a new terminal and enter:'
                                }
                                pre { code '$ curl -s get.sdkman.io | bash' }
                                p {
                                    yield 'Follow the instructions on-screen to complete installation.'
                                    br()
                                    yield 'Open a new terminal or type the command:'
                                }
                                pre { code '$ source "$HOME/.sdkman/bin/sdkman-init.sh"' }
                                p 'Then install the latest stable Groovy:'
                                pre { code '$ sdk install groovy' }
                                p 'After installation is complete and you\'ve made it your default version, test it with:'
                                pre { code '$ groovy -version' }
                                p 'That\'s all there is to it!'
                            }
                            hr(class: 'divider')

                            a(name: 'buildtools') {}
                            article {
                                h1 'From your build tools'
                                p 'If you wish to add Groovy as a dependency in your projects, you can refer to the Groovy JARs in the dependency section of your project build file descriptor:'
                                table(class: 'table') {
                                    thead {
                                        tr {
                                            th 'Gradle'
                                            th 'Maven'
                                            th 'Explanation'
                                        }
                                    }
                                    tbody {
                                        tr {
                                            td {
                                                code 'org.codehaus.groovy:groovy:x.y.z'
                                            }
                                            td {
                                                code '&lt;groupId&gt;org.codehaus.groovy&lt;/groupId&gt;'
                                                br()
                                                code '&lt;artifactId&gt;groovy&lt;/artifactId&gt;'
                                                br()
                                                code '&lt;version&gt;x.y.z&lt;/version&gt;'
                                            }
                                            td 'Just the core of Groovy without the modules (see below). Also includes jarjar\'ed versions of Antlr, ASM, and Commons-CLI.'
                                        }
                                        tr {
                                            td {
                                                code 'org.codehaus.groovy:groovy-$module:x.y.z'
                                            }
                                            td {
                                                code '&lt;groupId&gt;org.codehaus.groovy&lt;/groupId&gt;'
                                                br()
                                                code '&lt;artifactId&gt;groovy-$module&lt;/artifactId&gt;'
                                                br()
                                                code '&lt;version&gt;x.y.z&lt;/version&gt;'
                                            }
                                            td {
                                                code '"$module"'
                                                yield ' stands for the different optional groovy modules "ant", "bsf", "console", "docgenerator", "groovydoc", "groovysh", "jmx", "json", "jsr223", "nio", "servlet", "sql", "swing", "test", "templates", "testng" and "xml".'
                                                br()
                                                yield 'Example: '
                                                code '&lt;artifactId&gt;groovy-sql&lt;/artifactId&gt;'
                                            }
                                        }
                                        tr {
                                            td {
                                                code 'org.codehaus.groovy:groovy-all:x.y.z'
                                            }
                                            td {
                                                code '&lt;groupId&gt;org.codehaus.groovy&lt;/groupId&gt;'
                                                br()
                                                code '&lt;artifactId&gt;groovy-all&lt;/artifactId&gt;'
                                                br()
                                                code '&lt;version&gt;x.y.z&lt;/version&gt;'
                                                br()
                                                code '&lt;type&gt;pom&lt;/type&gt; &lt;!-- required JUST since Groovy 2.5.0 --&gt;'
                                            }
                                            td 'The core plus all the modules. Also includes <em>jarjar\'ed</em> versions of Antlr, ASM, Commons-CLI.\n' +
                                                    'In order to cater to the module system of Java 9+, only the individual jar files of the core and all modules will be provided since Groovy 2.5.0, i.e. the fat jar file <em>groovy-all-x.y.z.jar</em> will not be available.\n' +
                                                    'Optional dependencies are marked as optional.\n' +
                                                    'You may need to include some of the optional dependencies to use some features of Groovy, e.g. AntBuilder, GroovyMBeans...'
                                        }
                                    }
                                }
                                h3 'Maven repositories'
                                p "Groovy releases are downloadable from ${$a(href:'http://repo1.maven.org/maven2/org/codehaus/groovy/','Maven Central')} or ${$a(href:'http://jcenter.bintray.com/org/codehaus/groovy/','JCenter')}."
                                p "Groovy snapshots are downloadable from ${$a(href:'https://oss.jfrog.org/oss-snapshot-local/org/codehaus/groovy','JFrog OpenSource Snapshots repository')}."
                            }
                            hr(class: 'divider')

                            a(name: 'otherways') {}
                            article {
                                h1 'Other ways to get Groovy'
                                p {
                                    yield 'If you\'re on MacOS and have '
                                    a(href: 'http://brew.sh/', 'Homebrew')
                                    yield ' installed, you can install Groovy with:'
                                    pre { code 'brew install groovy' }
                                    yield 'If you\'re on MacOS and have '
                                    a(href: 'http://www.macports.org/', 'MacPorts')
                                    yield ' installed, you can install Groovy with:'
                                    pre { code 'sudo port install groovy' }
                                    yield 'If you\'re using Docker, Groovy is available on '
                                    a(href: 'https://hub.docker.com/_/groovy/', 'Docker Hub')
                                    yield '.'
                                    br()
                                    yield 'If you prefer to live on the bleeding edge, you can also grab the '
                                    a(href: 'https://github.com/apache/groovy', 'source code from GitHub')
                                    yield '.'
                                    br()
                                    yield 'If you are an IDE user, you can just grab the latest '
                                    a(href: 'ides.html', 'IDE plugin')
                                    yield ' and follow the plugin installation instructions.'
                                }
                            }
                            a(name: 'requirements') {}
                            article {
                                h1 'System requirements'
                                p {
                                    table(class: 'table') {
                                        thead {
                                            tr {
                                                th 'Groovy'
                                                th 'JVM Required (non-indy)'
                                                th 'JVM Required (indy) *'
                                            }
                                        }
                                        tbody {
                                            tr {
                                                td { b '3.0 - current' }
                                                td '1.8+'
                                                td '1.8+'
                                            }
                                            tr {
                                                td { b '2.5 - 2.6' }
                                                td '1.7+'
                                                td '1.7+'
                                            }
                                            tr {
                                                td { b '2.3 - 2.4' }
                                                td '1.6+'
                                                td '1.7+'
                                            }
                                            tr {
                                                td { b '2.0 - 2.2' }
                                                td '1.5+'
                                                td '1.7+'
                                            }
                                            tr {
                                                td { b '1.6 - 1.8' }
                                                td '1.5+'
                                                td 'N/A'
                                            }
                                            tr {
                                                td { b '1.0 - 1.5' }
                                                td '1.4+'
                                                td 'N/A'
                                            }
                                        }
                                    }
                                    yield '* If you plan to use invoke dynamic support, please read the '
                                    a(href: "indy.html", 'support information')
                                    yield '.'
                                }
                            }
                        }
                    }
                }
            }
        }
