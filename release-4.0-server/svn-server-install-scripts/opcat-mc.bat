mkdir %1\common
mkdir %1\common\conf
svnadmin create %1\Systems
copy /Y svnserve.conf %1\Systems\conf
copy users %1\common\conf
copy auth %1\common\conf



