//===============================================================
// Advent of Code 2022, Day 7: No Space Left On Device
//===============================================================

class Filesystem {

    FilesystemDir rootDir
    FilesystemDir currDir

    List<FilesystemDir> dirList = []

    int sum = 0

    Filesystem(input) {

        input.eachLine { line ->

            if (line.startsWith('$')) {
                if (line.drop(2).startsWith('cd')) {
                    def dir = line.drop(5)
                    if (currDir == null) {
                        currDir = new FilesystemDir(dir, null)
                        dirList << currDir
                        if (dir == '/') {
                            rootDir = currDir
                        }
                    } else {
                        if (dir == '..') {
                            currDir = currDir.parent
                        } else if (dir == '/') {
                            currDir = rootDir
                        } else {
                            def subDir = currDir.subdirs[dir]
                            if (subDir != null) {
                                currDir = subDir   
                            } else {
                                subDir = new FilesystemDir(dir, currDir)
                                dirList << subDir
                                currDir.subdirs[dir] = subDir
                                currDir = subDir
                            }
                        }
                    }
                }
            } else {
                def token = line.split(' ')
                if (token[0] == 'dir') {
                    def dir = token[1]
                    def subDir = currDir.subdirs[dir]
                    if (subDir == null) {
                        subDir = new FilesystemDir(dir, currDir)
                        dirList << subDir
                        currDir.subdirs[dir] = subDir

                    }
                } else {
                    def fname = token[1]
                    def size = token[0] as int
                    def file = currDir.files[fname]
                    if (file == null) {
                        currDir.files[fname] = new FilesystemFile(fname, size)
                    }
                }
            }
        }

        getSize(rootDir)
    }

    def getSize(FilesystemDir dir) {

        int size = 0

        dir.subdirs.each { name, subdir ->
            size += getSize(subdir)
        }
        dir.files.each { name, f ->
            size += f.size
        }

        dir.size = size

        if (size <= 100000) {
            sum += size
        }

        size
    }

    def chooseDirToDelete() {

        int spaceAvailable = 70000000
        int unusedSpaceNeeded = 30000000

        int unusedSpace = spaceAvailable - rootDir.size
        int additionalSpaceNeeded = unusedSpaceNeeded - unusedSpace

        if (additionalSpaceNeeded > 0) {
            dirList.findAll {it.size >= additionalSpaceNeeded}.sort {a, b -> a.size <=> b.size}[0].size
        } else {
            null
        }
    }

    String toString() {
        def stringList = []
        dirToString(rootDir, 0, stringList)
        stringList.join('\n') 
    }

    def dirToString(dir, level, stringList) {

        stringList << ' '.multiply(level*2) + dir.name+ ' (dir)'

        dir.subdirs.each{ name, subdir ->
            dirToString(subdir, level + 1, stringList)
        }

        dir.files.each{ name, f ->
            stringList << ' '.multiply((level + 1)*2) + f.name + " (file, size = $f.size)"
        }
    }
}

class FilesystemDir {

    String name
    FilesystemDir parent
    Map<String, FilesystemFile> files = [:]
    Map<String, FilesystemDir> subdirs = [:]
    int size

    FilesystemDir(name, parent) {
        this.name = name
        this.parent = parent
    }
}

class FilesystemFile {
    String name
    int size

    FilesystemFile(name, size) {
        this.name = name
        this.size = size
    }
}

//-----------------------------------------------
// Test

def input = '''$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k'''

def fs = new Filesystem(input)

assert fs.sum == 95437

assert fs.chooseDirToDelete() == 24933642

println 'Test ok'

//-----------------------------------------------
// Puzzle

input = Puzzle.getInput(2022, 7)

fs = new Filesystem(input)

//println fs.toString()

Puzzle.printResults fs.sum, fs.chooseDirToDelete()
