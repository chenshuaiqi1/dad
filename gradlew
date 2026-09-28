#!/bin/sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a symlink
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD="maximum"

warn () {
    echo "$*"
} >&2

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if [ "$darwin" = "true" ] ; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = "true" -o "$msys" = "true" ] ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`

    JAVACMD=`cygpath --unix "$JAVACMD"`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name .gradle 2>/dev/null | head -1`
    if [ -n "$ROOTDIRSRAW" ] ; then
        ROOTDIRS=`cygpath --path --mixed "$ROOTDIRSRAW"`
        CLASSPATH=`cygpath --path --mixed "$CLASSPATH:$ROOTDIRS"`
    fi

    if [ -n "$JAVA_HOME" ] ; then
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    fi

    # isMinGw4 in context of cygwin or msys (keep this in sync with gradle)
    # As it is not possible to do it inside the conditional when writing to stdin below the label 'isMinGw4' is not reachable when running cygpath in the middle of cat
    isMinGw4=false
    case "`uname`" in
      MSYS* )
        isMinGw4=true
        ;;
    esac

    # traverse filesystem from d: to z: and back to d: (it is faster)
    func_mcase_symlink() {
        case "`uname`" in
          CYGWIN*)
            input_path=`cygpath --unix "$1"`
            ;;
          *)
            input_path="$1"
            ;;
        esac
        backp=`expr $input_path : '/mnt/\([a-z]\).*'`
        if [ -z "$backp" ]; then
            backp=`expr $input_path : '/cygdrive/\([a-z]\).*'`
        fi
        if [ -n "$backp" ]; then
            echo "`cygpath --mixed /mnt/$backp`$input_path"
        else
            echo `cygpath --mixed "$input_path"`
        fi
    }

    new_path=`func_mcase_symlink "$input_path"`
    input_path="$new_path"
    if [ -z "$input_path" ]; then
        # couldn't change directory to the exact dir. Try cd'ing to the rightmost existing
        # directory in the path.
        dir_path=`expr "$input_path" : '\(.*\)/[^/]*$'`
        while [ ! -d "$dir_path" ]
        do
            dir_path=`expr "$dir_path" : '\(.*\)/[^/]*$'`
        done
        if [ -z "$dir_path" ]; then
            dir_path=/
        fi
        cd "$dir_path"
        new_path=`func_mcase_symlink "$PWD/$user_path_converted/"..."
        # that we must stop.
        if [ -z "$new_path" ]; then
            # The path is an existing file, we just use dirname to extract the directory,
            # prepend ./ and hope it works.
            if [ ! -e "$input_path" ]; then
                echo "INPUT ERROR: Path does not exist: $input_path" >&2
                exit 1
            fi
            new_path=`dirname "$input_path"`
            new_path=`expr "X$new_path" : 'X\(.*\)'`
            if [ ! -z "$new_path" ]; then
                new_path="$new_path"
            fi
        fi
        user_path_converted="$new_path"
    fi

    if [ -z "$CLASSPATH" ] ; then
        echo "Error: CLASSPATH is empty" >&2
        exit 1
    fi
    if [ -n "$JAVA_HOME" ] ; then
        JAVA_HOME="`(cd "$JAVA_HOME" 2>/dev/null && pwd )`"
    fi
    JAVA_HOME_CYGPATH=`cygpath --path --mixed "$JAVA_HOME"`
    classpath=`cygpath --path --mixed "$CLASSPATH"`
    JAVACMD=`cygpath --unix "$JAVACMD"`

    dir_path=`expr "$JAVA_HOME_CYGPATH" : '\(.*\)/bin'`
    new_path=`func_mcase_symlink "$dir_path"`
    JAVA_HOME="`(cd "$new_path" 2>/dev/null && pwd )`/bin"
    classpath="`cygpath --path --mixed $JAVA_HOME/../lib/tools.jar:$classpath`"
fi

# For Mingw, ensure paths are in MSYS format, just to make sure they all are
if [ "$MSYSTEM" = "MSYS" ]; then
    APP_HOME=`cd "$APP_HOME"; pwd -W`
    CLASSPATH=`cd "$CLASSPATH"; pwd -W`
    JAVA_HOME=`cd "$JAVA_HOME"; pwd -W`
fi

if [ -z "$JAVA_HOME" ]; then
  die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=`save "$@"`

# Collect all arguments for the java command, following the shell quoting and substitution rules
eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$APP_ARGS"

exec "$JAVACMD" "$@"
