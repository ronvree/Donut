module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (0)) (reg1),
    WriteInstr (reg1) (DirAddr 9),
    Load (ImmValue (0)) (reg2),
    WriteInstr (reg2) (DirAddr 10),
    Load (ImmValue (0)) (reg3),
    WriteInstr (reg3) (DirAddr 11),
    Load (ImmValue (0)) (reg4),
    WriteInstr (reg4) (DirAddr 12),
    TestAndSet (DirAddr 0),
    Receive (reg5),
    Branch (reg5) (Rel (2)),
    Jump (Rel (-3)),
    TestAndSet (DirAddr 1),
    Receive (reg5),
    Branch (reg5) (Rel (2)),
    Jump (Rel (-3)),
    TestAndSet (DirAddr 2),
    Receive (reg5),
    Branch (reg5) (Rel (2)),
    Jump (Rel (-3)),
    ReadInstr (DirAddr 0),
    Receive (reg5),
    Branch (reg5) (Rel (-2)),
    ReadInstr (DirAddr 1),
    Receive (reg5),
    Branch (reg5) (Rel (-2)),
    ReadInstr (DirAddr 2),
    Receive (reg5),
    Branch (reg5) (Rel (-2)),
    ReadInstr (IndAddr (reg0)),
    Receive (reg0),
    EndProg
    ]

thread1 :: [Instruction]
thread1 = [    Load (ImmValue (0)) (regSprID),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (2)),
    Jump (Rel (-3)),
    Load (ImmValue (1)) (reg1),
    WriteInstr (reg1) (DirAddr 10),
    Load (ImmValue (1)) (reg2),
    WriteInstr (reg2) (DirAddr 12),
    ReadInstr (DirAddr 11),
    Receive (reg3),
    ReadInstr (DirAddr 12),
    Receive (reg4),
    Load (ImmValue (1)) (reg5),
    Compute Equal (reg4) (reg5) (reg6),
    Compute And (reg3) (reg6) (reg7),
    Branch (reg7) (Rel (2)),
    Jump (Abs 19),
    Jump (Abs 9),
    Nop,
    ReadInstr (DirAddr 9),
    Receive (reg8),
    Load (ImmValue (1)) (reg9),
    Compute Add (reg8) (reg9) (reg10),
    WriteInstr (reg10) (DirAddr 9),
    WriteInstr (reg0) (IndAddr (regSprID)),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (-2)),
    EndProg
    ]

thread2 :: [Instruction]
thread2 = [    Load (ImmValue (1)) (regSprID),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (2)),
    Jump (Rel (-3)),
    Load (ImmValue (0)) (reg1),
    WriteInstr (reg1) (DirAddr 10),
    Load (ImmValue (1)) (reg2),
    WriteInstr (reg2) (DirAddr 11),
    Load (ImmValue (0)) (reg3),
    WriteInstr (reg3) (DirAddr 12),
    ReadInstr (DirAddr 10),
    Receive (reg4),
    ReadInstr (DirAddr 12),
    Receive (reg5),
    Load (ImmValue (0)) (reg6),
    Compute Equal (reg5) (reg6) (reg7),
    Compute And (reg4) (reg7) (reg8),
    Branch (reg8) (Rel (2)),
    Jump (Abs 21),
    Jump (Abs 11),
    Nop,
    WriteInstr (reg0) (IndAddr (regSprID)),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (-2)),
    EndProg
    ]

thread3 :: [Instruction]
thread3 = [    Load (ImmValue (2)) (regSprID),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (2)),
    Jump (Rel (-3)),
    ReadInstr (DirAddr 9),
    Receive (reg1),
    Load (ImmValue (1)) (reg2),
    Compute Add (reg1) (reg2) (reg3),
    WriteInstr (reg3) (DirAddr 9),
    Load (ImmValue (0)) (reg4),
    WriteInstr (reg4) (DirAddr 11),
    WriteInstr (reg0) (IndAddr (regSprID)),
    ReadInstr (IndAddr (regSprID)),
    Receive reg1,
    Branch reg1 (Rel (-2)),
    EndProg
    ]


main = sysTest [program,thread1,thread2,thread3]