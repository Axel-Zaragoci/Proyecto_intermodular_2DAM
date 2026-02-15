import subprocess
import sys

try:
    p = subprocess.run(['git','log','--no-merges','--numstat','--pretty=%aN'], capture_output=True, text=True, check=False)
except Exception as e:
    print('ERROR running git:', e)
    sys.exit(2)

if p.returncode != 0 and not p.stdout:
    print(p.stderr.strip())
    sys.exit(1)

lines = p.stdout.splitlines()
ins = {}
delc = {}
author = None
for line in lines:
    if not line:
        continue
    if '\t' in line:
        parts = line.split('\t')
        if len(parts) < 3:
            continue
        a = parts[0]
        b = parts[1]
        try:
            ai = int(a)
        except:
            ai = 0
        try:
            di = int(b)
        except:
            di = 0
        if author is None:
            continue
        ins[author] = ins.get(author, 0) + ai
        delc[author] = delc.get(author, 0) + di
    else:
        author = line
        if author not in ins:
            ins[author] = 0
            delc[author] = 0

# Print header
print('Autor\tInserciones\tEliminaciones')
for author, ai in sorted(ins.items(), key=lambda x: x[1], reverse=True):
    print(f"{author}\t{ai}\t{delc.get(author,0)}")
